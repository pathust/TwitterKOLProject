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

    private String extractUserName() {
        WebElement userNameElement = wait.until(
                presenceOfElementLocated(
                        By.xpath("//div[contains(@data-testid, 'UserName')]//span/span"))
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
    public void extractData(String userLink) throws InterruptedException {
        System.out.println("Extracting data from " + userLink);
        driver.get(userLink);
        Thread.sleep(3000);

        String username = extractUserName();
        int followingCount = extractFollowingCount();
        int followersCount = extractFollowersCount();

        System.out.print("User: " + username + "\n");
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
                User newUser = new User();
                newUser.setProfileLink(navigator.getLink(userCell));
                newUser.setVerified(isVerified);
                userDataHandler.addUser("KOLs.json", newUser);
            }
            navigator.scrollDown();
        }

        userDataHandler.saveData(filePath);
    }
}
