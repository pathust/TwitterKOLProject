package scraper.extractor;

import model.Tweet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import scraper.navigation.Navigator;
import scraper.storage.StorageHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static utils.Math.toInt;

public class TweetDataExtractor extends DataExtractor<Tweet> implements Extractor<Tweet> {

    private static final int RETRY_ATTEMPTS = 3;

    public TweetDataExtractor(WebDriver driver, Navigator navigator, StorageHandler<Tweet> storageHandler) {
        super(driver, navigator, storageHandler);
    }

    @Override
    protected WebElement getFirstCell() {
        WebElement tweetCell = null;
        boolean success = false;
        while (!success) {
            try {
                tweetCell = wait.until(presenceOfElementLocated(
                        By.xpath("//article[contains(@data-testid, 'tweet')]")));
                success = true;
            }
            catch (Exception e) {
                System.out.println("Finding tweet failed, retrying...");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return tweetCell;
    }

    @Override
    protected WebElement nextCell(WebElement tweetCell) {
        if (tweetCell == null) {
            System.out.println("Next cell is null");
            return getFirstCell();
        }

        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            try {
                WebElement parentDiv = tweetCell.findElement(
                        By.xpath("./ancestor::div[@data-testid='cellInnerDiv']"));
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

    @Override
    protected Tweet extractItem(WebElement tweetCell) {
        String authorUsername = extractAuthorUsername(tweetCell);
        String authorProfileLink = extractAuthorProfileLink(tweetCell);
        String tweetLink = extractTweetLink(tweetCell);
        String content = extractContent(tweetCell);
        int commentCount = extractCount("reply");
        int repostCount = extractCount("retweet");
        int likeCount = extractCount("like");

        Tweet tweet = new Tweet(tweetLink, authorProfileLink, repostCount);
        tweet.setAuthorUsername(authorUsername);
        tweet.setContent(content);
        tweet.setCommentCount(commentCount);
        tweet.setLikeCount(likeCount);
        return tweet;
    }

    @Override
    protected void Write(Tweet tweet) {
        System.out.println("Writing tweet: " + tweet.getAuthorUsername() + " posted " + tweet.getTweetLink());
    }

    @Override
    public void extractData(String tweetLink) throws IOException {
        out.println("Extracting data from " + tweetLink);
        driver.get(tweetLink);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Tweet tweet = storageHandler.get("Tweet.json", tweetLink);
        if (tweet == null) {
            out.println("Error: Tweet not found in Tweet.json for link: " + tweetLink);
            return;
        }

        try {
            WebElement repostButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//button[@data-testid='retweet']")));
            repostButton.click();

            WebElement viewQuotesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[text()='View Quotes']")));
            viewQuotesOption.click();

            WebElement viewRepostsOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//span[text()='Reposts']")));
            viewRepostsOption.click();

        } catch (Exception e) {
            out.println("Error: Unable to click 'Repost' or 'View Quotes' button.");
            e.printStackTrace();
            return;
        }

        List<Tweet> repostList = new ArrayList<>();
        List<String> repostLinks = new ArrayList<>();


        for (Tweet repost : repostList) {
            repostLinks.add(repost.getAuthorProfileLink());
        }

        tweet.setRepostList(repostLinks);

        try {
            storageHandler.add("Tweet.json", tweet);
        } catch (IOException e) {
            out.println("Error: Unable to save tweet data.");
            e.printStackTrace();
        }
    }

    private String extractAuthorUsername(WebElement tweetCell) {
        return tweetCell.findElement(By.xpath(".//div[@data-testid='User-Name']/div[1]")).getText();
    }

    private String extractAuthorProfileLink(WebElement tweetCell) {
        WebElement authorProfileLinkElement = tweetCell.findElement(By.xpath(".//div[@data-testid='User-Name']/div[1]//a"));
        return authorProfileLinkElement.getAttribute("href");
    }

    private String extractTweetLink(WebElement tweetCell) {
        WebElement tweetLinkElement = tweetCell.findElement(By.xpath(".//a[contains(@href, 'status')]"));
        return tweetLinkElement.getAttribute("href");
    }

    private String extractContent(WebElement tweetCell) {
        WebElement contentElement = tweetCell.findElement(By.xpath("//div[@data-testid='tweetText']"));
        return contentElement != null ? contentElement.getText() : "";
    }

    private int extractCount(String attributeValue) {
        String xpathExpression = "//*[@data-testid='" + attributeValue + "']";
        WebElement interactElement = wait.until(
                presenceOfElementLocated(By.xpath(xpathExpression))
        );
        return toInt(interactElement.getText());
    }
}