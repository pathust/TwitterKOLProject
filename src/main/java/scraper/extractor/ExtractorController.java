package scraper.extractor;

import UI.waiting.WaitingScene;
import javafx.application.Platform;
import model.User;
import model.Tweet;
import org.openqa.selenium.WebDriver;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;
import storage.main.StorageHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;
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
    }

    public List<User> getUsers(String filePath) throws IOException {
        return storageHandler.getAll(USER, filePath)
                .stream()
                .filter(item -> item instanceof User)
                .map(item -> (User) item)
                .toList();
    }

    public List<Tweet> getTweets(String filePath) throws IOException {
        return storageHandler.getAll(TWEET, filePath)
                .stream()
                .filter(item -> item instanceof User)
                .map(item -> (Tweet) item)
                .toList();
    }

    public void scrapeUsersData(List<User> users) throws IOException {
        for (User user : users) {
            if (user == null) {
                System.out.println("Skipping scrape user ");
                continue;

            }
            else {
                System.out.println("Scraping user " + user.getUsername());
            }

            Platform.runLater(() -> WaitingScene.updateStatus("Collecting " + user.getProfileLink()));

            //Lấy các tweet tại trang cá nhân
            driver.get(user.getProfileLink());
            System.out.println("Extracting tweets from user: " + user.getUsername());
            //navigator.navigateToSection("highlights");
            extractTweetsFromProfileLink("Tweet.json", user, 3);

            // Lưu dữ liệu tweet vào file
            List<Tweet> userTweets = getTweets("Tweet.json");
            System.out.println("Number of tweets for user " + user.getUsername() + ": " + userTweets.size());

            //Làm việc với user
            userDataExtractor.extractData(user.getProfileLink());
            storageHandler.save(USER,"KOLs.json");
        }
    }

    public void scrapeTweetsData(List<Tweet> tweets) throws IOException {
        for (Tweet tweet : tweets) {
            if (tweet == null) {
                System.out.println("Skipping scrape tweet ");
                continue;

            }
            else {
                System.out.println("Scraping tweet " + tweet.getAuthorProfileLink());
            }
            tweetDataExtractor.extractData(tweet.getTweetLink());
            extractRepostList(tweet);
            storageHandler.save(TWEET, "Tweet.json");
        }
    }

    private List<User> extractInitialKOLsTo(String filePath) throws IOException {
        System.out.println("Start collecting user data...");

        navigator.navigateToSection("user");

        List <User> users = userDataExtractor.extractItems(10, true);
        for (User user : users) {
            System.out.println("User " + user.getUsername());
            storageHandler.add(USER, filePath, user);
        }
        return users;
    }

    private List<Tweet> extractInitialTweetsTo(String filePath) throws IOException {
        System.out.println("Start collecting tweet data...");

        List <Tweet> tweets = tweetDataExtractor.extractItems(7, true);
        for (Tweet tweet : tweets) {
            storageHandler.add(TWEET, filePath,tweet);
        }
        return tweets;
    }

    // Thêm phương thức để lấy tweet từ trang các nhân của các KOLs
    public void extractTweetsFromProfileLink(String filePath, User user, int maxListSize) throws IOException {
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

    public void extractRepostList(Tweet tweet) throws IOException {
        List<User> repostList = userDataExtractor.extractItems(3, false);
        List<String> repostLinks = new ArrayList<>();

        for (User repost : repostList) {
            repostLinks.add(repost.getProfileLink());
        }

        tweet.setRepostList(repostLinks);

        try {
            storageHandler.add(TWEET, "Tweet.json", tweet);
        } catch (IOException e) {
            out.println("Error: Unable to save tweet data.");
            e.printStackTrace();

        }
    }

    public void extractData() throws IOException, InterruptedException {
        String searchResultLink = driver.getCurrentUrl();

        // Extract data from tweets
        Thread.sleep(3000);

        List<Tweet> tweets = extractInitialTweetsTo("Tweet.json");

        while (!driver.getCurrentUrl().contains("search")) {
            driver.get(searchResultLink);
        }

        // Extract data from users
        List<User> users = extractInitialKOLsTo("KOLs.json");
        for(Tweet tweet: tweets){
            User user = new User (tweet.getAuthorProfileLink(), tweet.getAuthorUsername());
            storageHandler.add(USER, "KOLs.json", user);
        }
        System.out.println("Number of users: " + users.size());
        scrapeUsersData(users);

        // Extract data from tweets
        System.out.println("Number of tweets: " + tweets.size());
        scrapeTweetsData(tweets);

    }
}
