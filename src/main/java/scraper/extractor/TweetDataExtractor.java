package scraper.extractor;

import model.Tweet;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import scraper.navigation.Navigator;
import storage.StorageHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    }

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
    protected Tweet extractItem(String filePath, String xpathExpression, boolean addToStorage) throws IOException {
        int tweetType = checkTweet(xpathExpression);
        System.out.println(tweetType);

        int commentCount = extractCount(xpathExpression, "reply");
        int repostCount = extractCount(xpathExpression, "retweet");
        int likeCount = extractCount(xpathExpression, "like");

        String repostLink = null;
        if (tweetType == 3) {
            repostLink = formatUserLink(extractTweetLink(xpathExpression));
            xpathExpression = xpathExpression + "//div[div/span[contains(text(), 'Quote')]]";
        }
        else if (tweetType == 2) {
            repostLink = navigator.getLink(
                    xpathExpression
                    + "//a[span[contains(text(), 'reposted')]]");
        }

        String tweetLink = formatTweetLink(extractTweetLink(xpathExpression));
        if (tweetLink == null) {
            return null;
        }
        String authorProfileLink = formatUserLink(tweetLink);
        String authorUsername = extractUserName(xpathExpression);
        String content = extractContent(xpathExpression);

        System.out.println("author: " + authorUsername);
        Tweet tweet = new Tweet(tweetLink, authorProfileLink);
        tweet.setAuthorUsername(authorUsername);
        tweet.setContent(content);
        tweet.setCommentCount(commentCount);
        tweet.setLikeCount(likeCount);
        tweet.setRepostCount(repostCount);

        if (repostLink != null && !tweet.getAuthorProfileLink().equals(repostLink)) {
            List<String> repostList = new ArrayList<>();
            repostList.add(repostLink);
            tweet.setRepostList(repostList);
        }

        if (addToStorage)
            storageHandler.add(TWEET, filePath, tweet);
        return tweet;
    }

    @Override
    public void extractData(String filePath, String tweetLink) throws IOException {
        out.println("Extracting data from " + tweetLink);
        navigator.wait(3000);

        Tweet tweet = (Tweet) storageHandler.get(TWEET, "Tweet", tweetLink);
        if (tweet == null) {
            out.println("Error: Tweet not found in Tweet.json for link: " + tweetLink);
            return;
        }
        storageHandler.add(TWEET, "Tweet", tweet);

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

    private String formatUserLink(String input) {
        if (input == null) {
            return null;
        }
        if (input.contains("/status/")) {
            int statusIndex = input.indexOf("/status/");
            System.out.println(input.substring(0, statusIndex));
            return input.substring(0, statusIndex);
        }
        return input;
    }

    private String formatTweetLink(String input) {
        if (input == null) {
            return null;
        }
        if (input.contains("/status/")) {
            int statusIndex = input.indexOf("/status/");
            String afterStatus = input.substring(statusIndex);
            int nextSlashIndex = afterStatus.indexOf("/", "/status/".length());
            if (nextSlashIndex == -1) {
                return input;
            }
            System.out.println(input.substring(0, nextSlashIndex));
            return input.substring(0, statusIndex + nextSlashIndex);
        }

        return input;
    }

    private String extractUserName(String parentXpath) {
        String xpathExpression = parentXpath + "//div[@data-testid='User-Name']/div[1]//span[not(*)]";
        for (int i = 0; i < 3; i++) {
            try {
                return navigator.getText(xpathExpression);
            }
            catch (Exception e) {
                System.out.println("retry " + xpathExpression);
                navigator.wait(2000);
            }
        }
        return "Unknown";
    }

    private String extractTweetLink(String parentXpath) {
        String xpathExpression = parentXpath + "//a[contains(@href, 'status')]";
        try {
            return navigator.getLink(xpathExpression);
        }
        catch (Exception e) {
            return null;
        }
    }

    private String extractContent(String parentXpath) {
        String xpathExpression = parentXpath + "//div[@data-testid='tweetText']";
        try {
            return navigator.getText(xpathExpression);
        }
        catch (Exception e) {
            return null;
        }
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

    private boolean elementExist(String xpathExpression) {
        return !driver.findElements(By.xpath(xpathExpression)).isEmpty();
    }


    private int checkTweet(String parentXpath){
        String repostedXpath = parentXpath + "//a[span[contains(text(), 'reposted')]]";
        String quoteXpath = parentXpath + "//div[div/span[contains(text(), 'Quote')]]";
        if(elementExist(repostedXpath)){
            return 2;
        }
        else if(elementExist(quoteXpath)){
            return 3;
        }
        else{
            return 1;
        }
    }
}