package scraper.extractor;

import model.Tweet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import scraper.navigation.Navigator;
import storage.StorageHandler;

import java.io.IOException;
import java.time.Duration;

import static java.lang.System.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static utils.Math.toInt;
import static utils.ObjectType.TWEET;

public class TweetDataExtractor extends DataExtractor<Tweet> implements Extractor<Tweet> {
    private static final int RETRY_ATTEMPTS = 3;

    public TweetDataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
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
    protected Tweet extractItem(String xpathExpression, boolean addToStorage) {
        String authorUsername = extractAuthorUsername(xpathExpression);
        String authorProfileLink = extractAuthorProfileLink(xpathExpression);
        String tweetLink = extractTweetLink(xpathExpression);
        String content = extractContent(xpathExpression);
        int commentCount = extractCount(xpathExpression, "reply");
        int repostCount = extractCount(xpathExpression,"retweet");
        int likeCount = extractCount(xpathExpression,"like");

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

        Tweet tweet = (Tweet) storageHandler.get(TWEET, "Tweet.json", tweetLink);
        if (tweet == null) {
            out.println("Error: Tweet not found in Tweet.json for link: " + tweetLink);
            return;
        }

        try {
            WebElement repostButton = wait.until(elementToBeClickable(By.xpath(".//button[@data-testid='retweet']")));
            repostButton.click();

            WebElement viewQuotesOption = wait.until(elementToBeClickable(By.xpath(".//span[text()='View Quotes']")));
            viewQuotesOption.click();

            WebElement viewRepostsOption = wait.until(elementToBeClickable(By.xpath(".//span[text()='Reposts']")));
            viewRepostsOption.click();

        } catch (Exception e) {
            out.println("Error: Unable to click 'Repost' or 'View Quotes' button.");
            e.printStackTrace();
        }
    }

    private String extractAuthorUsername(String parentXPath) {
        String xpathExpression = parentXPath + "//div[@data-testid='User-Name']/div[1]";
        return driver.findElement(By.xpath(xpathExpression)).getText();
    }

    private String extractAuthorProfileLink(String parentXPath) {
        String xpathExpression = parentXPath + "//div[@data-testid='User-Name']/div[1]//a";
        return driver.findElement(By.xpath(xpathExpression)).getAttribute("href");
    }

    private String extractTweetLink(String parentXPath) {
        String xpathExpression = parentXPath + "//a[contains(@href, 'status')]";
        return driver.findElement(By.xpath(xpathExpression)).getAttribute("href");
    }

    private String extractContent(String parentXPath) {
        String xpathExpression = parentXPath + "//div[@data-testid='tweetText']";
        return driver.findElement(By.xpath(xpathExpression)).getText();
    }

    private int extractCount(String parentXpath, String attributeValue) {
        String xpathExpression = parentXpath + "//*[@data-testid='" + attributeValue + "']//span";
        try {
            WebElement interactElement = wait.until(
                    presenceOfElementLocated(By.xpath(xpathExpression))
            );
            return toInt(interactElement.getText());
        }
        catch (Exception e) {
            return 0;
        }
    }
}