package scraper.extractor;

import ui.waiting.WaitingScene;
import javafx.application.Platform;
import model.User;
import model.Tweet;
import org.openqa.selenium.WebDriver;
import scraper.navigation.Navigator;
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
        List<String> links = storageHandler.getUnprocessedItemUniqueKeys(USER, filePath);
        for (String profileLink : links) {
            if (profileLink == null) {
                continue;
            }
            Platform.runLater(() -> WaitingScene.updateStatus("Collecting " + profileLink));

            extractTweetsFromProfileLink("Tweet", profileLink, maxTweetListSize);
            userDataExtractor.extractData(filePath, profileLink);
            storageHandler.transferToMainStorage(USER, filePath, storageHandler.get(USER, filePath, profileLink));
        }
    }

    public void scrapeTweetsData(String filePath, int maxRepostListSize) throws IOException, InterruptedException {
        for (String tweetLink : storageHandler.getUnprocessedItemUniqueKeys(TWEET, filePath)) {
            Platform.runLater(() -> WaitingScene.updateStatus("Collecting " + tweetLink));

            tweetDataExtractor.extractData(filePath, tweetLink);
            extractRepostList(filePath, tweetLink, maxRepostListSize);

            storageHandler.transferToMainStorage(TWEET, filePath, storageHandler.get(TWEET, filePath, tweetLink));
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

    public void extractData(boolean isResume) throws IOException, InterruptedException {
        // Extract data from tweets
        if (!isResume) {
            extractInitialTweetsTo("Tweet", 1000);
            navigator.navigateToSection("user");
            extractInitialKOLsTo("KOLs", 1010);
        }

        // Scrape data
        scrapeUsersData("KOLs", 10);
        scrapeTweetsData("Tweet", 20);
    }
}
