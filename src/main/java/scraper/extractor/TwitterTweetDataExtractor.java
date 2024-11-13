package scraper.extractor;

import model.Tweet;
import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import scraper.storage.TweetDataHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static model.Tweet.toInt;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class TwitterTweetDataExtractor implements TweetDataExtractor {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private final TweetDataHandler tweetDataHandler;

    private static final int MAX_SCROLLS = 2;
    private static final int RETRY_ATTEMPTS = 3;

    public TwitterTweetDataExtractor(WebDriver driver, Navigator navigator, TweetDataHandler tweetDataHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.navigator = navigator;
        this.tweetDataHandler = tweetDataHandler;
    }

    private WebElement findNextTweetCell(WebElement tweetCell) {
        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            try {
                WebElement parentDiv = tweetCell.findElement(By.xpath("./ancestor::div[@data-testid='cellInnerDiv']"));
                return parentDiv.findElement(
                        By.xpath("(following-sibling::div[@data-testid='cellInnerDiv'])//article[contains(@data-testid, 'tweet')]"));
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed, retrying...");
                wait.withTimeout(Duration.ofSeconds(1)); // Shorter wait for retries
            }
        }
        System.out.println("Next TweetCell not found after " + RETRY_ATTEMPTS + " attempts.");
        return null;
    }

    private String extractUserOfTweetName(WebElement tweetCell) {
        return tweetCell.findElement(By.xpath(".//span[not(*)]")).getText();
    }

    private String extractTweetLink(WebElement tweetCell) {
        WebElement tweetLinkElement = tweetCell.findElement(By.xpath(".//a[contains(@href, '/status/')]"));
        return tweetLinkElement.getAttribute("href");
    }

    private String extractContent(WebElement tweetCell) {
        WebElement contentElement = tweetCell.findElement(By.xpath(".//div[@data-testid='tweetText']"));
        return contentElement != null ? contentElement.getText() : "";
    }

    private int extractRepostCount() {
        try {
            WebElement repostCountElement = wait.until(
                    presenceOfElementLocated(By.xpath("//button[@aria-label[contains(., 'reposts')]]//span")));
            return toInt(repostCountElement.getText());
        } catch (Exception e) {
            System.out.println("Error extracting repost count.");
            return 0;
        }
    }

    private String extractPostTime(WebElement tweetCell) {
        WebElement postTimeElement = tweetCell.findElement(By.xpath(".//time[@datetime]"));
        return postTimeElement != null ? postTimeElement.getAttribute("datetime") : "";
    }

    @Override
    public void extractData(String tweetLink, int repostCountThreshold, int maxNewUser) throws IOException {
        System.out.println("Extracting data from " + tweetLink);
        driver.get(tweetLink);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int repostCount = extractRepostCount();
        System.out.print("Repost: " + repostCount + "\n");

        // Lấy tweet từ JSON và kiểm tra null
        Tweet tweet = tweetDataHandler.getTweet("Tweet.json", tweetLink);
        if (tweet == null) {
            System.out.println("Error: Tweet not found in Tweet.json for link: " + tweetLink);
            return;
        }
        tweet.setRepostCount(repostCount);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click Repost và View Quotes
        try {
            WebElement repostButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-testid='retweet']")));
            repostButton.click();

            WebElement viewQuotesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='View Quotes']")));
            viewQuotesOption.click();
        } catch (Exception e) {
            System.out.println("Error: Unable to click 'Repost' or 'View Quotes' button.");
            e.printStackTrace();
            return;
        }

        // Lấy danh sách repost và lưu vào tweet
        List<Tweet> repostList = extractTweets(Math.min(tweet.getRepostCount(), repostCountThreshold), maxNewUser);
        List<String> repostLinks = new ArrayList<>();
        for (Tweet repost : repostList) {
            tweetDataHandler.addTweet("Tweet.json", repost);
            repostLinks.add(repost.getUserLink());
        }
        tweet.setRepostList(repostLinks);

        // Lưu tweet đã cập nhật
        try {
            tweetDataHandler.addTweet("Tweet.json", tweet);
        } catch (IOException e) {
            System.out.println("Error: Unable to save tweet data.");
            e.printStackTrace();
        }
    }

    /*public void extractData(String tweetLink, int repostCountThreshold, int maxNewUser) throws IOException {
        System.out.println("Extracting data from " + tweetLink);
        driver.get(tweetLink);

        int repostCount = extractRepostCount();
        System.out.print("Repost: " + repostCount + "\n");

        Tweet tweet = tweetDataHandler.getTweet("Tweet.json", tweetLink);
        tweet.setRepostCount(repostCount);

        try {
            WebElement repostButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-testid='retweet']")));
            repostButton.click();

            WebElement viewQuotesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='View Quotes']")));
            viewQuotesOption.click();

            navigator.clickButton("Reposts");
        } catch (Exception e) {
            System.out.println("Error: Unable to click 'Repost' or 'View Quotes' button.");
            e.printStackTrace();
            return;
        }

        List<Tweet> repostList = extractTweets(Math.min(repostCount, repostCountThreshold), maxNewUser);
        List<String> repostLinks = new ArrayList<>();
        for (Tweet repost : repostList) {
            tweetDataHandler.addTweet("Tweet.json", repost);
            repostLinks.add(repost.getUserLink());
        }
        tweet.setRepostList(repostLinks);

        try {
            tweetDataHandler.addTweet("Tweet.json", tweet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    @Override
    public List<Tweet> extractTweets(int maxListSize, int maxNewUsers) {
        int countNewUser = 0;
        List<Tweet> tweetsList = new ArrayList<>();
        if (maxListSize == 0) {
            return tweetsList;
        }

        WebElement userCell = wait.until(presenceOfElementLocated(
                By.xpath("//article[contains(@data-testid, 'tweet')]")));

        while (tweetsList.size() < maxListSize) {
            String usernameText = extractUserOfTweetName(userCell);
            String userLink = navigator.getLink(userCell);
            String tweetLink = extractTweetLink(userCell);
            int repostCount = extractRepostCount();
            try {
                if (tweetDataHandler.userExists("Tweet.json", userLink) || countNewUser < maxNewUsers) {
                    Tweet newTweet = new Tweet(tweetLink, userLink, repostCount);
                    tweetsList.add(newTweet);
                    System.out.println("Added user: " + usernameText);

                    if (!tweetDataHandler.userExists("Tweet.json", userLink)) {
                        countNewUser++;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error checking user existence for " + usernameText);
                throw new RuntimeException(e);
            }

            userCell = findNextTweetCell(userCell);
            if (userCell == null) {
                break;
            } else {
                navigator.scrollToElement(userCell);
            }
        }
        return tweetsList;
    }
}
