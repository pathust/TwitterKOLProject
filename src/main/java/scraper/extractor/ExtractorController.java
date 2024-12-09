package scraper.extractor;

import UI.waiting.WaitingScene;
import javafx.application.Platform;
import model.User;
import model.Tweet;
import org.openqa.selenium.WebDriver;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;
import storage.Storage;
import storage.StorageHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utils.ObjectType.TWEET;
import static utils.ObjectType.USER;

public class ExtractorController {
    private final WebDriver driver;
    private final Navigator navigator;
    private final StorageHandler storageHandler;
    private final Extractor<User> userDataExtractor;
    private final Extractor<Tweet> tweetDataExtractor;

    public ExtractorController(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        this.driver = driver;
        this.navigator = navigator;
        this.storageHandler = storageHandler;
        this.userDataExtractor = new UserDataExtractor(driver, navigator, storageHandler);
        this.tweetDataExtractor = new TweetDataExtractor(driver, navigator, storageHandler);
        navigator.wait(5000);
    }

    public void scrapeUsersData(String filePath, int maxTweetListSize) throws IOException, InterruptedException {
        for (String profileLink : storageHandler.getUnprocessedItemUniqueKeys(USER, filePath)) {
            Platform.runLater(() -> WaitingScene.updateStatus("Collecting " + profileLink));

            driver.get(profileLink);
            extractTweetsFromProfileLink("Tweet", profileLink, maxTweetListSize);
            userDataExtractor.extractData(filePath, profileLink);

            storageHandler.transferToMainStorage(USER,filePath, storageHandler.get(USER, "KOLs", profileLink));
        }
    }

    public void scrapeTweetsData(String filePath, int maxRepostListSize) throws IOException, InterruptedException {
        for (String tweetLink : storageHandler.getUnprocessedItemUniqueKeys(TWEET, filePath)) {
            Platform.runLater(() -> WaitingScene.updateStatus("Collecting " + tweetLink));

            driver.get(tweetLink);
            tweetDataExtractor.extractData(filePath, tweetLink);
            extractRepostList(filePath, tweetLink, maxRepostListSize);

            storageHandler.transferToMainStorage(TWEET, filePath, storageHandler.get(TWEET, "Tweet", tweetLink));
        }
    }

    private List<User> extractInitialKOLsTo(String filePath, int maxListSize) throws IOException, InterruptedException {
        return userDataExtractor.extractItems(filePath, maxListSize, true);
    }

    private List<Tweet> extractInitialTweetsTo(String filePath, int maxListSize) throws IOException, InterruptedException {
        return tweetDataExtractor.extractItems(filePath, maxListSize, true);
    }

    public void extractTweetsFromProfileLink(String filePath, String profileLink, int maxListSize) throws IOException, InterruptedException {
        List<Tweet> tweets = tweetDataExtractor.extractItems(filePath, maxListSize, true);
        for (Tweet tweet : tweets) {
            if (!tweet.getAuthorProfileLink().equals(profileLink)) {
                List<String> repostList = new ArrayList<>();
                repostList.add(profileLink);
                tweet.setRepostList(repostList);
            }
            storageHandler.add(TWEET, filePath, tweet);
        }
    }

    public Tweet extractRepostList(String filePath, String tweetLink, int maxListSize) throws IOException, InterruptedException {
        List<User> repostList = userDataExtractor.extractItems(filePath, maxListSize, false);

        List<String> repostLinks = new ArrayList<>();
        for (User repost : repostList) {
            repostLinks.add(repost.getProfileLink());
        }

        Tweet tweet = (Tweet) storageHandler.get(TWEET, "Tweet", tweetLink);
        tweet.setRepostList(repostLinks);

        return tweet;
    }

    public void extractData() throws IOException, InterruptedException {
        // Extract data from tweets
        extractInitialTweetsTo("Tweet", 100);

        navigator.navigateToSection("user");

        // Extract data from users
        extractInitialKOLsTo("KOLs", 100);

        // Scrape data
        scrapeUsersData("KOLs", 20);
        scrapeTweetsData("Tweet", 10);
    }
}
