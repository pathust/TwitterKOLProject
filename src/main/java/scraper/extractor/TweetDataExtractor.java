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

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static utils.Formator.formatTweetLink;
import static utils.Formator.formatUserLink;
import static utils.Math.toInt;
import static utils.ObjectType.TWEET;

public class TweetDataExtractor extends DataExtractor<Tweet> implements Extractor<Tweet> {
    public TweetDataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        super(driver, navigator, storageHandler);
    }

    @Override
    protected WebElement getFirstCell() {
        String xpathExpression = "//article[contains(@data-testid, 'tweet')]";
        for (int i = 0; i < 3; i++) {
            try {
                return wait.until(presenceOfElementLocated(By.xpath(xpathExpression)));
            } catch (Exception e) {
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
                    xpathExpression + "//a[span[contains(text(), 'reposted')]]");
        }

        String tweetLink = formatTweetLink(extractTweetLink(xpathExpression));
        if (tweetLink == null) {
            return null;
        }
        String authorProfileLink = formatUserLink(tweetLink);
        String authorUsername = extractUserName(xpathExpression);
        String content = extractContent(xpathExpression);

        Tweet tweet = new Tweet(tweetLink, authorProfileLink, authorUsername, content,
                likeCount, commentCount, repostCount);

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
    public void extractData(String filePath, String tweetLink) {
        navigator.wait(500);
        driver.get(tweetLink + "/retweets");
    }

    private String extractUserName(String parentXpath) {
        String xpathExpression = parentXpath + "//div[@data-testid='User-Name']/div[1]//span[not(*)]";
        return navigator.getText(xpathExpression);
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
        return navigator.getText(xpathExpression);
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