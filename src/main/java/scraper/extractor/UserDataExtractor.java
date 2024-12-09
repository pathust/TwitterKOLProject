package scraper.extractor;

import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import scraper.navigation.Navigator;
import storage.StorageHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static utils.Math.getLastInt;
import static utils.Math.toInt;
import static utils.ObjectType.USER;

public class UserDataExtractor extends DataExtractor<User> implements Extractor<User> {
    public UserDataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        super(driver, navigator, storageHandler);
    }

    @Override
    protected WebElement getFirstCell() {
        System.out.println("first uCell");
        for (int i = 0; i < 3; i++) {
            try {
                String xpathExpression = "//button[@data-testid='UserCell']";
                return wait.until(presenceOfElementLocated(By.xpath(xpathExpression)));
            }
            catch (Exception e) {
                navigator.wait(2000);
            }
        }
        return null;
    }

    @Override
    protected WebElement nextCell(WebElement userCell) {
        if (userCell == null) {
            return getFirstCell();
        }

        String parentDivXpathExpression = "./ancestor::div[@data-testid='cellInnerDiv']";
        String nextCellXpathExpression = "(following-sibling::div[@data-testid='cellInnerDiv'])//button[@data-testid='UserCell']";
        for (int i = 0; i < 3; i++) {
            try {
                WebElement parentDiv = userCell.findElement(By.xpath(parentDivXpathExpression));
                return parentDiv.findElement(By.xpath(nextCellXpathExpression));
            } catch (Exception e) {
                navigator.wait(2000);
            }
        }
        return null;
    }

    @Override
    protected User extractItem(String xpathExpression, boolean addToStorage) {
        if (addToStorage)
            navigator.clickButton(xpathExpression, "Follow");

        String username = extractUserName(xpathExpression);
        String profileLink = extractProfileLink(xpathExpression);
        return new User(profileLink, username);
    }

    @Override
    public void extractData(String userLink) throws IOException, InterruptedException {
        System.out.println("Extracting data from " + userLink);
        navigator.wait(2000);

        String followersCount = extractCount("followers");
        String followingCount = extractCount("following");

        int knownFollowersCount = extractKnownFollowersCount();
        navigator.navigateToSection("followers_you_follow");
        List<User> followersList = extractItems(knownFollowersCount, false);

        List<String> followersLinks = new ArrayList<>();
        for (User user : followersList) {
            followersLinks.add(user.getProfileLink());
        }

        User newUser = (User) storageHandler.get(USER, "KOLs", userLink);
        if (newUser != null) {
            newUser.setFollowersCount(toInt(followersCount));
            newUser.setFollowingCount(toInt(followingCount));
            newUser.setFollowersList(followersLinks);
            storageHandler.add(USER, "KOLs", newUser);
        }
    }

    private String extractUserName(String parentXPath) {
        String xpathExpression = parentXPath + "//div[last()]//a//span";
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                WebElement userNameElement = wait.until(presenceOfElementLocated(By.xpath(xpathExpression)));
                return userNameElement.getText();
            } catch (Exception e) {
                attempt++;
                System.out.println("Retry attempt " + attempt + ": Error extracting username");
                if (attempt == maxRetries) {
                    throw new RuntimeException("Failed to extract username after " + maxRetries + " attempts", e);
                }
            }
        }

        return null;
    }

    private String extractProfileLink(String parentXPath) {
        String xpathExpression = parentXPath + "//div[last()]//a";
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                WebElement userNameElement = driver.findElement(By.xpath(xpathExpression));
                return userNameElement.getAttribute("href");
            } catch (Exception e) {
                attempt++;
                System.out.println("Retry attempt " + attempt + ": Error extracting profile link");
                if (attempt == maxRetries) {
                    throw new RuntimeException("Failed to extract profile link after " + maxRetries + " attempts", e);
                }
            }
        }
        return null;
    }

    private String extractCount(String section) {
        String xpathExpression = "//a[contains(@href, '" + section + "')]//span/span";
        try {
            WebElement countElement = wait.until(presenceOfElementLocated(By.xpath(xpathExpression)));
            return countElement.getText();
        } catch (Exception e) {
            return "0";
        }
    }

    private int extractKnownFollowersCount() {
        String xpathExpression = "//div[contains(text(),'Followed by')]";
        try {
            WebElement knownFollowersDiv = driver.findElement(By.xpath(xpathExpression));
            xpathExpression += "/span";
            int res = driver.findElements(By.xpath(xpathExpression)).size();
            res += getLastInt(knownFollowersDiv.getText());
            return res;
        }
        catch (Exception e) {
            return 0;
        }
    }
}
