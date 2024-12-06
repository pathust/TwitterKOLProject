package scraper.extractor;

import model.Tweet;
import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import scraper.navigation.Navigator;
import storage.main.StorageHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static utils.Math.toInt;
import static utils.ObjectType.USER;

public class UserDataExtractor extends DataExtractor<User> implements Extractor<User> {
    private Extractor<Tweet> tweetExtractor;
    public UserDataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        super(driver, navigator, storageHandler);
        tweetExtractor = new TweetDataExtractor(driver, navigator, storageHandler);
    }

    @Override
    protected WebElement getFirstCell() {
        return wait.until(
                        presenceOfElementLocated(
                                By.xpath("//button[@data-testid='UserCell']")
                        )
        );
    };

    @Override
    protected WebElement nextCell(WebElement userCell) {
        if (userCell == null) {
            return getFirstCell();
        }

        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement parentDiv = userCell.findElement(
                        By.xpath("./ancestor::div[@data-testid='cellInnerDiv']"));

                return parentDiv.findElement(
                        By.xpath("(following-sibling::div[@data-testid='cellInnerDiv'])//button[@data-testid='UserCell']"));
            } catch (Exception e) {
                System.out.println("Attempt " + (attempt + 1) + " failed, retrying...");
                waitBeforeRetry(2000);
            }
        }
        System.out.println("Next UserCell not found after 3 attempts.");
        return null;
    }

    @Override
    protected User extractItem(String xpathExpression, boolean addToStorage) {
        if (addToStorage)
            navigator.clickButton(xpathExpression, "Follow");

        String username = extractUserName(xpathExpression);
        String profileLink = extractProfileLink(xpathExpression);
        User user = new User(profileLink, username);
        return user;
    }

    @Override
    protected void Write(User user) {
        System.out.println("Writing user: " + user.getUsername() + " " + user.getProfileLink());
    }

    @Override
    public void extractData(String userLink) {
        System.out.println("Extracting data from " + userLink);
        driver.get(userLink);
        waitBeforeRetry(2000);

        checkAndClickRestrictedButton();
        String followersCount = extractCount("followers");
        String followingCount = extractCount("following");

        try {
            int knownFollowersCount = extractKnownFollowersCount();
            System.out.println("Known followers count: " + knownFollowersCount);
            navigator.navigateToSection("followers_you_follow");
            List<User> followersList = extractItems(knownFollowersCount, false);

            List<String> followersLinks = new ArrayList<>();
            for (User user : followersList) {
                storageHandler.add(USER, "KOLs.json", user);
                followersLinks.add(user.getProfileLink());
            }

            User newUser = (User) storageHandler.get(USER, "KOLs.json", userLink);
            if (newUser != null) {
                newUser.setFollowersCount(toInt(followersCount));
                newUser.setFollowingCount(toInt(followingCount));
                newUser.setFollowersList(followersLinks);
                storageHandler.add(USER, "KOLs.json", newUser);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error updating user data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Cannot extract data from " + userLink);
        }
    }

    private String extractUserName(String parentXPath) {
        try {
            String xpathExpression = parentXPath + "//div[last()]//a//span";
            WebElement userNameElement = driver.findElement(By.xpath(xpathExpression));
            return userNameElement.getText();
        } catch (Exception e) {
            System.out.println("Unable to extract username.");
            return "Unknown";
        }
    }

    private String extractProfileLink(String parentXPath) {
        try {
            String xpathExpression = parentXPath + "//div[last()]//a";
            WebElement userNameElement = driver.findElement(By.xpath(xpathExpression));
            return userNameElement.getAttribute("href");
        } catch (Exception e) {
            System.out.println("Unable to extract username.");
            return "Unknown";
        }
    }

    private void checkAndClickRestrictedButton() {
        waitBeforeRetry(1000);
        try {
            List<WebElement> restrictedButtons = driver.findElements(
                    By.xpath("//button[div/span/span[text()='Yes, view profile']]"));
            if (!restrictedButtons.isEmpty()) {
                restrictedButtons.get(0).click();
                System.out.println("Clicked on restricted button.");
            }
        } catch (Exception e) {
            System.out.println("No restricted button found.");
        }
    }

    private String extractCount(String section) {
        try {
            WebElement countElement = wait.until(
                    presenceOfElementLocated(By.xpath("//a[contains(@href, '" + section + "')]//span/span")));
            return countElement.getText();
        } catch (Exception e) {
            System.out.println("Failed to extract " + section + " count.");
            return "0";
        }
    }

    private int extractKnownFollowersCount() {
        String xpathExpression = "//div[contains(text(),'Followed by')]";
        WebElement knownFollowersDiv = null;
        try {
            knownFollowersDiv = driver.findElement(By.xpath(xpathExpression));
        }
        catch (Exception e) {
            return 0;
        }

        String text = knownFollowersDiv.getText();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        int res = 0;
        while (matcher.find()) {
            res = Integer.parseInt(matcher.group());
        }

        xpathExpression += "/span";
        res += driver.findElements(By.xpath(xpathExpression)).size();

        return res;
    }

    private void waitBeforeRetry(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
