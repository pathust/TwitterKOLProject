package scraper.extractor;

import model.Tweet;
import model.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import scraper.storage.TweetDataHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static model.User.toInt;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class TwitterTweetDataExtractor implements TweetDataExtractor{
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private final TweetDataHandler tweetDataHandler;

    private static final int MAX_SCROLLS = 2;

    public TwitterTweetDataExtractor(WebDriver driver, Navigator navigator, TweetDataHandler tweetDataHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
        this.tweetDataHandler = tweetDataHandler;
    }

    private List<WebElement> findNextTweet() {
        return wait.until(presenceOfAllElementsLocatedBy(
                By.xpath("//article[contains(@data-testid, 'tweet')]")));
    }

    private String extractUserNameOfTweet() {
        WebElement userNameElement = wait.until(
                presenceOfElementLocated(
                        By.xpath("//div[contains(@data-testid, 'UserName')]//span/span"))
        );
        return userNameElement.getText();
    }

    private int extractRetweetCount() {
        WebElement retweetCountElement = wait.until(
                presenceOfElementLocated(
                        By.xpath("//article[contains(@data-testid, 'retweet')]"))
        );
        return toInt(retweetCountElement.getText());
    }


    private void extractTimeOfTweet() {
        for (int scrollCount = 0; scrollCount < MAX_SCROLLS; scrollCount++) {
            List<WebElement> tweetTimes = driver.findElements(
                    By.xpath("//article[contains(@data-testid, 'tweet')]/following::time"));
            for (WebElement tweetTime : tweetTimes) {
                String postDate = tweetTime.getAttribute("datetime");
                System.out.println("Posted date: " + postDate + "\n");
            }
        }
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
    public void extractData(String tweetLink) throws InterruptedException, IOException {
        System.out.println("Extracting data from " + tweetLink);
        driver.get(tweetLink);
        Thread.sleep(5000);

        String username = extractUserNameOfTweet();
        int retweetCount = extractRetweetCount();

        System.out.print("User: " + username + "\n");
        System.out.print("Repost: " + retweetCount + "\n");
        extractTimeOfTweet();
    }

    @Override
    public void extractTweetTo(String filePath, boolean isVerified) throws InterruptedException, IOException {
        for (int scrollCount = 0; scrollCount < MAX_SCROLLS; scrollCount++) {
            Thread.sleep(3000);
            List<WebElement> tweets = findNextTweet();

            if (tweets.isEmpty()) {
                System.out.println("No tweet found.");
                break;
            }

            for (int i = 0; i < tweets.size(); i++) {
                WebElement tweet = wait.until(presenceOfElementLocated(
                        By.xpath("(//article[contains(@data-testid, 'tweet')])[" + (i + 1) + "]")));
                Tweet newTweet = new Tweet(navigator.getLink(tweet));
                newTweet.setVerified(isVerified);
                tweetDataHandler.addTweet(filePath, newTweet);
            }
            navigator.scrollDown();
        }

        tweetDataHandler.saveData(filePath);
    }
}
