package scraper.extractor;

import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import scraper.storage.UserDataHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
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

    public WebElement findNextUserCell(WebElement userCell) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement parentDiv = userCell.findElement(
                        By.xpath("./ancestor::div[@data-testid='cellInnerDiv']"));

                return parentDiv.findElement(
                        By.xpath("(following-sibling::div[@data-testid='cellInnerDiv'])//button[@data-testid='UserCell']"));
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
    public void extractData(String userLink, int followingCountThreshold, int maxNewUser) throws IOException {
        System.out.println("Extracting data from " + userLink);
        driver.get(userLink);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        checkAndClickRestrictedButton();
        String followersCount = extractFollowersCount();
        String followingCount = extractFollowingCount();

        System.out.print("Following: " + followingCount + "\n");
        System.out.print("Followers: " + followersCount + "\n");


        User user = userDataHandler.getUser("KOLs.json", userLink);
        user.setFollowersCount(followersCount);
        user.setFollowingCount(followingCount);

        navigator.navigateToSection("following");
        List<User> followingList = extractUsers(false, min(user.getFollowingCount(), followingCountThreshold), maxNewUser);
        List<String> followingLinks = new ArrayList<>();
        for (User following : followingList) {
            userDataHandler.addUser("KOLs.json", following);
            followingLinks.add(following.getProfileLink());
        }
        user.setFollowingList(followingLinks);
        try {
            userDataHandler.addUser("KOLs.json", user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        extractPotentialKOLs();
    }

    @Override
    public List<User> extractUsers(boolean isVerified, int maxListSize, int maxNewUsers) {
        int countNewUser = 0;
        int attempt = 0;
        List<User> usersList = new ArrayList<>();
        if (maxListSize == 0) {
            return usersList;
        }
        WebElement userCell = wait.until(presenceOfElementLocated(
                By.xpath("//button[@data-testid = 'UserCell']")));

        while (true) {
            String username = extractUserName(userCell);
            String profileLink = navigator.getLink(userCell);
            boolean checkUser;
            try {
                checkUser = userDataHandler.userExists("KOLs.json", profileLink);
            } catch (IOException e) {
                System.out.println("User " + username + " not found.");
                throw new RuntimeException(e);
            }

            if (checkUser){
                User newUser = new User(profileLink, username, isVerified);
                usersList.add(newUser);
                System.out.println("Add user to usersList " + username);
            }
            else if(countNewUser < maxNewUsers){
                User newUser = new User(profileLink, username, isVerified);
                usersList.add(newUser);
                System.out.println("Add user to usersList " + username);
                countNewUser++;
            }
            attempt++;
            if (attempt >= maxListSize) {
                System.out.print("Done !");
                break;
            }

            userCell = findNextUserCell(userCell);
            if(userCell == null){
                break;
            }
            else {
                navigator.scrollToElement(userCell);
            }
        }

        return usersList;
    }
}
