package scraper.extractor;

import UI.waiting.WaitingScene;
import javafx.application.Platform;
import model.User;
import model.Tweet;
import org.openqa.selenium.WebDriver;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;
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

    public ExtractorController(WebDriver driver) {
        this.driver = driver;
        this.navigator = new WebNavigator(driver);
        this.storageHandler = new StorageHandler();
        this.userDataExtractor = new UserDataExtractor(driver, navigator, storageHandler);
        this.tweetDataExtractor = new TweetDataExtractor(driver, navigator, storageHandler);
        navigator.wait(5000);
    }

    public void scrapeUsersData(List<User> users, int maxTweetListSize) throws IOException, InterruptedException {
        for (User user : users) {
            if (user == null) {
                continue;

            }
            Platform.runLater(() -> WaitingScene.updateStatus("Collecting " + user.getProfileLink()));

            driver.get(user.getProfileLink());
            extractTweetsFromProfileLink("Tweet", user, maxTweetListSize);

            userDataExtractor.extractData(user.getProfileLink());
            storageHandler.transferToMainStorage(USER,"KOLs", user);
        }
    }

    public void scrapeTweetsData(List<Tweet> tweets, int maxRepostListSize) throws IOException, InterruptedException {
        for (Tweet tweet : tweets) {
            if (tweet == null) {
                continue;
            }

            driver.get(tweet.getTweetLink());
            tweetDataExtractor.extractData(tweet.getTweetLink());
            extractRepostList(tweet, maxRepostListSize);
            storageHandler.transferToMainStorage(TWEET, "Tweet", tweet);
        }
    }

    private List<User> extractInitialKOLsTo(String filePath, int maxListSize) throws IOException, InterruptedException {
        List <User> users = userDataExtractor.extractItems(maxListSize, true);
        for (User user : users) {
            storageHandler.add(USER, filePath, user);
        }
        storageHandler.save(USER, filePath);
        return users;
    }

    private List<Tweet> extractInitialTweetsTo(String filePath, int maxListSize) throws IOException, InterruptedException {
        List <Tweet> tweets = tweetDataExtractor.extractItems(maxListSize, true);
        System.out.println("copy " + tweets.size());
        for (Tweet tweet : tweets) {
            User user = new User (tweet.getAuthorProfileLink(), tweet.getAuthorUsername());
            storageHandler.add(USER, "KOLs", user);
            System.out.println(tweet.getTweetLink());
            storageHandler.add(TWEET, filePath, tweet);
            System.out.println("done " + tweet.getTweetLink());
        }
        System.out.println(tweets.size());
        storageHandler.save(USER, filePath);
        storageHandler.save(TWEET, filePath);
        return tweets;
    }

    public void extractTweetsFromProfileLink(String filePath, User user, int maxListSize) throws IOException, InterruptedException {
        List<Tweet> tweets = tweetDataExtractor.extractItems(maxListSize, true);
        for (Tweet tweet : tweets) {
            if (!tweet.getAuthorProfileLink().equals(user.getProfileLink())) {
                List<String> repostList = new ArrayList<>();
                repostList.add(user.getProfileLink());
                tweet.setRepostList(repostList);
            }
            storageHandler.add(TWEET, filePath, tweet);
        }

        storageHandler.save(TWEET, filePath);
    }

    public void extractRepostList(Tweet tweet, int maxListSize) throws IOException, InterruptedException {
        List<User> repostList = userDataExtractor.extractItems(maxListSize, false);

        List<String> repostLinks = new ArrayList<>();
        for (User repost : repostList) {
            repostLinks.add(repost.getProfileLink());
        }

        tweet.setRepostList(repostLinks);

        storageHandler.add(TWEET, "Tweet", tweet);
    }

    public void extractData() throws IOException, InterruptedException {
        // Extract data from tweets
        System.out.println("Extracting data... from tweet");
        //List<Tweet> tweets = extractInitialTweetsTo("Tweet", 100);
        List<Tweet> tweets = extractInitialTweetsTo("Tweet", 10);

        System.out.println(tweets.size());
        navigator.navigateToSection("user");

        // Extract data from users
        //List<User> users = extractInitialKOLsTo("KOLs", 1000);
        List<User> users = extractInitialKOLsTo("KOLs", 10);

        // Scrape data
        //scrapeUsersData(users, 20);
        //scrapeTweetsData(tweets, 10);
        scrapeUsersData(users, 3);
        scrapeTweetsData(tweets, 3);
    }
}
