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

public class TwitterTweetDataExtractor implements TweetDataExtractor {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private final TweetDataHandler tweetDataHandler;

    private static final int MAX_SCROLLS = 2;
    private static final int RETRY_ATTEMPTS = 3;

    public TwitterTweetDataExtractor(WebDriver driver, Navigator navigator, TweetDataHandler tweetDataHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
        this.tweetDataHandler = tweetDataHandler;
    }

    public WebElement findNextTweetCell(WebElement tweetCell) {
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

    private String extractContent(WebElement tweetCell) {
        WebElement contentElement = tweetCell.findElement(By.xpath(".//div[@data-testid='tweetText']"));
        return contentElement != null ? contentElement.getText() : "";
    }

    private int extractRepostCount() {
        WebElement repostCountElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='retweet']")));
        return toInt(repostCountElement.getText());
    }

    private String extractPostTime(WebElement tweetCell) {
        WebElement postTimeElement = tweetCell.findElement(By.tagName("time"));
        return postTimeElement != null ? postTimeElement.getAttribute("datetime") : "";
    }
}

