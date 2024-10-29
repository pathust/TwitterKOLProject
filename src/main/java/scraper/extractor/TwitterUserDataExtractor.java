package scraper.extractor;

import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import scraper.storage.UserDataHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static model.User.toInt;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class TwitterUserDataExtractor implements UserDataExtractor {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private final UserDataHandler userDataHandler;

    private static final int MAX_SCROLLS = 2;

    public TwitterUserDataExtractor(WebDriver driver, Navigator navigator, UserDataHandler userDataHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
        this.userDataHandler = userDataHandler;
    }

    private List<WebElement> findNextUserCells() {
        return wait.until(presenceOfAllElementsLocatedBy(
                By.xpath("//button[@data-testid='UserCell']")));
    }

    private String extractUserName(WebElement userCell) {
        WebElement userNameElement = userCell.findElement(
                By.xpath(".//span[not(*)]") // Find the leaf <span> within userCell
        );
        return userNameElement.getText();
    }

    private int extractFollowingCount() {
        WebElement followingCountElement = wait.until(
                presenceOfElementLocated(
                        By.xpath("//a[contains(@href, 'following')]//span/span"))
        );
        return toInt(followingCountElement.getText());
    }

    private int extractFollowersCount() {
        WebElement followersCountElement = wait.until(
                presenceOfElementLocated(
                        By.xpath("//a[contains(@href, 'followers')]//span/span"))
        );
        return toInt(followersCountElement.getText());
    }

    private void extractPotentialKOLs() {
        for (int scrollCount = 0; scrollCount < MAX_SCROLLS; scrollCount++) {
            List<WebElement> potentialKOLs = driver.findElements(
                    By.xpath("//span[@data-testid='socialContext']/following::span[2]"));
            System.out.print("Number of potential KOLs found: " + potentialKOLs.size() + "\n");

            for (WebElement potentialKOL : potentialKOLs) {
                String kolName = potentialKOL.getText();
                System.out.print("Potential KOL: " + kolName + "\n");
            }
        }
    }

    @Override
    public void extractData(String userLink) throws InterruptedException, IOException {
        System.out.println("Extracting data from " + userLink);
        driver.get(userLink);
        Thread.sleep(5000);

        int followingCount = extractFollowingCount();
        int followersCount = extractFollowersCount();

        System.out.print("Following: " + followingCount + "\n");
        System.out.print("Followers: " + followersCount + "\n");

        extractPotentialKOLs();
    }

    @Override
    public void extractUserTo(String filePath, boolean isVerified) throws InterruptedException, IOException {
        for (int scrollCount = 0; scrollCount < MAX_SCROLLS; scrollCount++) {
            Thread.sleep(3000);
            List<WebElement> userCells = findNextUserCells();

            if (userCells.isEmpty()) {
                System.out.println("No user cells found.");
                break;
            }

            for (int i = 0; i < userCells.size(); i++) {
                WebElement userCell = wait.until(presenceOfElementLocated(
                        By.xpath("(//button[@data-testid='UserCell'])[" + (i + 1) + "]")));
                User newUser = new User(navigator.getLink(userCell));
                newUser.setVerified(isVerified);
                userDataHandler.addUser(filePath, newUser);
            }
            navigator.scrollDown();
        }

        userDataHandler.saveData(filePath);
    }

    public List<User> extractUser(boolean isVerified, int maxListSize) throws InterruptedException {
        List<User> usersList = new ArrayList<>();
        while (usersList.size() < maxListSize) {
            Thread.sleep(3000);

            WebElement userCell = wait.until(presenceOfElementLocated(
                    By.xpath("//button[@data-testid = 'UserCell']["+ usersList.size() +"]//span[not(*)]")));
            String username = extractUserName(userCell);
            User newUser = new User(navigator.getLink(userCell));
            newUser.setUsername(username);
            newUser.setVerified(isVerified);
            usersList.add(newUser);
            System.out.println("Add user to usersList " + username);
            WebElement nextUserCell = findNextUserCell(userCell);
            if(nextUserCell != null) navigator.scrollToElement(nextUserCell);
            else break;
        }

        return usersList;
    }

    public WebElement findNextUserCell(WebElement currentUserCell) {
        WebElement nextUserCell = null;

        // Try to find the next sibling UserCell up to 10 times
        for (int attempt = 1; attempt <= 10; attempt++) {
            try {
                // XPath to find the next sibling UserCell element containing a leaf <span>
                nextUserCell = wait.until(presenceOfElementLocated(
                        By.xpath(".//following-sibling::button[@data-testid='UserCell']//span[not(*)]")));

                if (nextUserCell != null) {
                    System.out.println("Found next UserCell on attempt: " + attempt);
                    return nextUserCell; // Return the found element
                }
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed, retrying...");
            }

            // Brief wait before the next attempt
            try {
                Thread.sleep(500); // 500 ms delay between attempts
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted during wait: " + e.getMessage());
            }
        }

        System.out.println("Next UserCell not found after 10 attempts.");
        return null; // Return null if the element wasn't found after 10 tries
    }

}
