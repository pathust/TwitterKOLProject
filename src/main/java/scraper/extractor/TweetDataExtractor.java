package scraper.extractor;

import model.Tweet;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import scraper.navigation.Navigator;
import storage.StorageHandler;

import java.io.IOException;

import static java.lang.System.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static utils.Math.toInt;
import static utils.ObjectType.TWEET;

public class TweetDataExtractor extends DataExtractor<Tweet> implements Extractor<Tweet> {
    public TweetDataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        super(driver, navigator, storageHandler);
    }

    @Override
    protected WebElement getFirstCell() {
        System.out.println("first cell...");
        for (int i = 0; i < 3; i++) {
            try {
                String xpathExpression = "//article[contains(@data-testid, 'tweet')]";
                return wait.until(presenceOfElementLocated(By.xpath(xpathExpression)));
            }
            catch (Exception e) {
                navigator.wait(2000);
            }
        }
        return null;
    };

    @Override
    protected WebElement nextCell(WebElement tweetCell) {
        if (tweetCell == null) {
            return getFirstCell();
        }

        String parentDivXpathExpression = "./ancestor::div[@data-testid='cellInnerDiv']";
        String nextCellXpathExpression = "(following-sibling::div[@data-testid='cellInnerDiv'])//article[contains(@data-testid, 'tweet')]";
        for (int i = 0; i < 3; i++) {
            try {
                WebElement parentDiv = tweetCell.findElement(By.xpath(parentDivXpathExpression));
                return parentDiv.findElement(By.xpath(nextCellXpathExpression));
            } catch (Exception e) {
                navigator.wait(2000);
            }
        }
        return null;
    }

    @Override
    protected Tweet extractItem(String xpathExpression, boolean addToStorage) {
        System.out.println("extract item...");
        String authorUsername = extractAuthorUsername(xpathExpression);
        String authorProfileLink = extractAuthorProfileLink(xpathExpression);
        String tweetLink = extractTweetLink(xpathExpression);
        String content = extractContent(xpathExpression);
        int commentCount = extractCount(xpathExpression, "reply");
        int repostCount = extractCount(xpathExpression,"retweet");
        int likeCount = extractCount(xpathExpression,"like");

        System.out.println("author: " + authorUsername);
        Tweet tweet = new Tweet(tweetLink, authorProfileLink, repostCount);
        tweet.setAuthorUsername(authorUsername);
        tweet.setContent(content);
        tweet.setCommentCount(commentCount);
        tweet.setLikeCount(likeCount);
        return tweet;
    }

    @Override
    public void extractData(String tweetLink) throws IOException {
        out.println("Extracting data from " + tweetLink);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Tweet tweet = (Tweet) storageHandler.get(TWEET, "Tweet", tweetLink);
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
            throw new RuntimeException(e);
        }
    }

    private String extractAuthorUsername(String parentXPath) {
        boolean success = false;
        while (!success) {
            try {
                String xpathExpression = parentXPath + "//div[@data-testid='User-Name']/div[1]";
                return driver.findElement(By.xpath(xpathExpression)).getText();
            }
            catch (Exception e) {
                navigator.wait(2000);
                System.out.println("error extracting author username");
            }
        }
        return null;
    }

    private String extractAuthorProfileLink(String parentXPath) {
        boolean success = false;
        while (!success) {
            try {
                String xpathExpression = parentXPath + "//div[@data-testid='User-Name']/div[1]//a";
                return driver.findElement(By.xpath(xpathExpression)).getAttribute("href");
            }
            catch (Exception e) {
                navigator.wait(2000);
                System.out.println("error extracting author profile link");
            }
        }
        return null;
    }

    private String extractTweetLink(String parentXPath) {
        boolean success = false;
        while (!success) {
            try {
                String xpathExpression = parentXPath + "//a[contains(@href, 'status')]";
                return driver.findElement(By.xpath(xpathExpression)).getAttribute("href");
            }
            catch (Exception e) {
                navigator.wait(2000);
                System.out.println("error extracting tweet link");
            }
        }
        return null;
    }

    private String extractContent(String parentXPath) {
        try {
            String xpathExpression = parentXPath + "//div[@data-testid='tweetText']";
            return driver.findElement(By.xpath(xpathExpression)).getText();
        }
        catch (Exception e) {
            navigator.wait(2000);
            System.out.println("error extracting tweet content");
        }
        return null;
    }

    private int extractCount(String parentXpath, String attributeValue) {
        String xpathExpression = parentXpath + "//*[@data-testid='" + attributeValue + "']//span";
        try {
            WebElement interactElement = driver.findElement(By.xpath(xpathExpression));
            return toInt(interactElement.getText());
        }
        catch (Exception e) {
            return 0;
        }
    }
}