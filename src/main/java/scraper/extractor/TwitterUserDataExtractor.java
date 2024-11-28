package scraper.extractor;

import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import scraper.storage.StorageHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class TwitterUserDataExtractor implements UserDataExtractor {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private final StorageHandler<User> userDataHandler;

    private static final int MAX_SCROLLS = 2;

    public TwitterUserDataExtractor(WebDriver driver, Navigator navigator, StorageHandler<User> userDataHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
        this.userDataHandler = userDataHandler;
    }

    public WebElement findNextUserCell(WebElement userCell) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement parentDiv = userCell.findElement(
                        By.xpath("./ancestor::div[@data-testid='cellInnerDiv']"));

                userCell = parentDiv.findElement(
                        By.xpath("(following-sibling::div[@data-testid='cellInnerDiv'])//button[@data-testid='UserCell']"));

                return userCell;
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed, retrying...");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Next UserCell not found after 3 attempts.");
        return null;
    }

    private String extractUserName(WebElement userCell) {
        WebElement userNameElement = userCell.findElement(
                By.xpath(".//span[not(*)]") // Find the leaf <span> within userCell
        );
        return userNameElement.getText();
    }

    private void checkAndClickRestrictedButton() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            WebElement restrictedButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[div/span/span[text()='Yes, view profile']]")));

            if(restrictedButton != null) {
                restrictedButton.click();
            }
        } catch (Exception e) {
            System.out.println("Restricted button not found or not clickable.");
        }
    }

    private String extractFollowingCount() {
        WebElement followingCountElement = wait.until(
                presenceOfElementLocated(
                        By.xpath("//a[contains(@href, 'following')]//span/span"))
        );
        return followingCountElement.getText();
    }

    private String extractFollowersCount() {
        WebElement followersCountElement = wait.until(
                presenceOfElementLocated(
                        By.xpath("//a[contains(@href, 'followers')]//span/span"))
        );
        return followersCountElement.getText();
    }

    @Override
    public void extractData(String userLink) {
        System.out.println("Extracting data from " + userLink);
        driver.get(userLink);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        checkAndClickRestrictedButton();
        String followersCount = extractFollowersCount();
        String followingCount = extractFollowingCount();

        System.out.print("Following: " + followingCount + "\n");
        System.out.print("Followers: " + followersCount + "\n");

        try {
            navigator.navigateToSection("followers_you_follow");

            List<User> followersList = extractUsers(false, -1, false);
            List<String> followersLinks = new ArrayList<>();
            for (User user : followersList) {
                userDataHandler.add("KOLs.json", user);
                followersLinks.add(user.getProfileLink());
            }
            User newUser = userDataHandler.get("KOLs.json", userLink);
            newUser.setFollowersCount(followersCount);
            newUser.setFollowingCount(followingCount);
            newUser.setFollowersList(followersLinks);
            try {
                userDataHandler.add("KOLs.json", newUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        catch (Exception e) {
            System.out.println("Cannot extract data from " + userLink);
        }
    }

    @Override
    public List<User> extractUsers(boolean isVerified, int maxListSize, boolean addToStorage) {
        int countNewUser = 0;
        List<User> usersList = new ArrayList<>();
        if (maxListSize == 0) {
            return usersList;
        }
        WebElement userCell = wait.until(presenceOfElementLocated(
                By.xpath("//button[@data-testid = 'UserCell']")));

        while (true) {
            String username = extractUserName(userCell);
            String profileLink = navigator.getLink(userCell);

            User newUser = new User(profileLink, username, isVerified);
            usersList.add(newUser);
            System.out.println(countNewUser + ". Add user to usersList " + username);

            if (addToStorage)
                navigator.clickButton(userCell, "Follow");

            if (++countNewUser == maxListSize) {
                break;
            }

            userCell = findNextUserCell(userCell);
            if(userCell == null) {
                break;
            }

            navigator.scrollToElement(userCell);
        }

        System.out.println("Users list size: " + usersList.size());

        return usersList;
    }

}
