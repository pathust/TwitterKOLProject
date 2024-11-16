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

import static java.lang.System.*;
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
                out.println("Attempt " + attempt + " failed, retrying...");
                wait.withTimeout(Duration.ofSeconds(1)); // Shorter wait for retries
            }
        }
        out.println("Next TweetCell not found after " + RETRY_ATTEMPTS + " attempts.");
        return null;
    }

    private WebElement findNextUserCell(WebElement tweetCell) {
        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            try {
                WebElement parentDiv = tweetCell.findElement(By.xpath("./ancestor::div[@data-testid='cellInnerDiv']"));
                return parentDiv.findElement(
                        By.xpath("(following-sibling::div[@data-testid='cellInnerDiv'])//button[@data-testid='UserCell']"));
            } catch (Exception e) {
                out.println("Attempt " + attempt + " failed, retrying...");
                wait.withTimeout(Duration.ofSeconds(1)); // Shorter wait for retries
            }
        }
        out.println("Next UserCell not found after " + RETRY_ATTEMPTS + " attempts.");
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
            out.println("Error extracting repost count.");
            return 0;
        }
    }

    private String extractPostTime(WebElement tweetCell) {
        WebElement postTimeElement = tweetCell.findElement(By.xpath(".//time[@datetime]"));
        return postTimeElement != null ? postTimeElement.getAttribute("datetime") : "";
    }

    @Override
    public void extractData(String tweetLink, int repostCountThreshold, int maxNewUser) throws IOException {
        out.println("Extracting data from " + tweetLink);
        driver.get(tweetLink);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int repostCount = extractRepostCount();
        out.print("Repost: " + repostCount + "\n");

        Tweet tweet = tweetDataHandler.getTweet("Tweet.json", tweetLink);
        if (tweet == null) {
            out.println("Error: Tweet not found in Tweet.json for link: " + tweetLink);
            return;
        }
        tweet.setRepostCount(repostCount);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement repostButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-testid='retweet']")));
            repostButton.click();

            WebElement viewQuotesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='View Quotes']")));
            viewQuotesOption.click();

            WebElement viewRepostsOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Reposts']")));
            viewRepostsOption.click();
        } catch (Exception e) {
            out.println("Error: Unable to click 'Repost' or 'View Quotes' button.");
            e.printStackTrace();
            return;
        }

        List<Tweet> repostList = extractEachTweet(Math.min(tweet.getRepostCount(), repostCountThreshold), maxNewUser);
        int countNewUsers = maxNewUser;
        int countRepost =0;
        List<String> repostLinks = new ArrayList<>();
        for (Tweet repost : repostList) {
            if (countRepost >= countNewUsers) {
                break;
            }
            tweetDataHandler.addTweet("Tweet.json", repost);
            repostLinks.add(repost.getUserLink());
            countRepost++;
        }
        tweet.setRepostList(repostLinks);

        try {
            tweetDataHandler.addTweet("Tweet.json", tweet);
        } catch (IOException e) {
            out.println("Error: Unable to save tweet data.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Tweet> extractEachTweet(int maxListSize, int maxNewUsers) {
        int countNewUser = 0;
        List<Tweet> tweetsList = new ArrayList<>();
        if (maxListSize == 0) {
            return tweetsList;
        }

        WebElement userCell = null;
        boolean success = false;int dem =0;
        while (!success) {
            try{
                dem =0;
                userCell = wait.until(presenceOfElementLocated(
                        By.xpath("//button[@data-testid='UserCell']")));
                success = true;
            }
            catch (Exception e) {
                dem++;
                System.out.println("Finding user failed, retrying...");
                if( dem ==3){
                    success = true;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        while (tweetsList.size() < maxListSize && countNewUser < maxNewUsers) {
            String usernameText = extractUserOfTweetName(userCell);
            String userLink = navigator.getLink(userCell);
            String tweetLink = userLink;
            int repostCount =0;
            try {
                if (tweetDataHandler.userExists("Tweet.json", userLink) || countNewUser < maxNewUsers) {
                    Tweet newTweet = new Tweet(tweetLink, userLink, repostCount);
                    tweetsList.add(newTweet);
                    out.println("Added user: " + usernameText);

                    if (!tweetDataHandler.userExists("Tweet.json", userLink)) {
                        countNewUser++;
                    }
                }
            } catch (IOException e) {
                out.println("Error checking user existence for " + usernameText);
                throw new RuntimeException(e);
            }
            countNewUser++;
            userCell = findNextUserCell(userCell);
            if (userCell == null) {
                break;
            } else {
                navigator.scrollToElement(userCell);
            }
        }
        return tweetsList;
    }


    @Override
    public List<Tweet> extractTweets(int maxListSize, int maxNewUsers) {
        int countNewUser = 0;
        List<Tweet> tweetsList = new ArrayList<>();
        if (maxListSize == 0) {
            return tweetsList;
        }

        WebElement tweetCell = null;
        boolean success = false;int dem =0;
        while (!success) {
            try{
                dem =0;
                tweetCell = wait.until(presenceOfElementLocated(
                        By.xpath("//article[contains(@data-testid, 'tweet')]")));
                success = true;
            }
            catch (Exception e) {
                dem++;
                System.out.println("Finding tweet failed, retrying...");
                if( dem ==3){
                    success = true;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        while (tweetsList.size() < maxListSize) {
            String usernameText = extractUserOfTweetName(tweetCell);
            String userLink = navigator.getLink(tweetCell);
            String tweetLink = extractTweetLink(tweetCell);
            //int repostCount = extractRepostCount();
            int repostCount =0;
            try {
                if (tweetDataHandler.userExists("Tweet.json", userLink) || countNewUser < maxNewUsers) {
                    Tweet newTweet = new Tweet(tweetLink, userLink, repostCount);
                    tweetsList.add(newTweet);
                    out.println("Added user: " + usernameText);

                    if (!tweetDataHandler.userExists("Tweet.json", userLink)) {
                        countNewUser++;
                    }
                }
            } catch (IOException e) {
                out.println("Error checking user existence for " + usernameText);
                throw new RuntimeException(e);
            }

            tweetCell = findNextTweetCell(tweetCell);
            if (tweetCell == null) {
                break;
            } else {
                navigator.scrollToElement(tweetCell);
            }
        }
        return tweetsList;
    }

}