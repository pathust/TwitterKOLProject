package scraper;

import UI.waiting.WaitingScene;
import model.User;
import model.Tweet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import scraper.authentication.Authenticator;
import scraper.authentication.TwitterAuthenticator;
import scraper.extractor.TweetDataExtractor;
import scraper.extractor.TwitterUserDataExtractor;
import scraper.extractor.TwitterTweetDataExtractor;
import scraper.extractor.UserDataExtractor;
import scraper.filtering.Filter;
import scraper.filtering.TwitterFilter;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;
import scraper.storage.TweetDataHandler;
import scraper.storage.UserDataHandler;
import scraper.storage.UserStorageManager;
import scraper.storage.TweetDataHandler;
import scraper.storage.TweetStorageManager;

import java.io.IOException;
import java.util.List;

import static java.lang.Integer.sum;


public class TwitterScraperController {
    private static WebDriver driver;
    private final Navigator navigator;
    private final Authenticator authenticator;
    private final Filter filter;
    private final UserDataExtractor userDataExtractor;
    private final UserDataHandler userDataHandler;
    private final TweetDataHandler tweetDataHandler;
    private final TweetDataExtractor tweetDataExtractor;

    public TwitterScraperController() {
        System.setProperty(
                "webdriver.chrome.driver",
                "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();
        this.navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = new TwitterFilter(driver, navigator);
        this.userDataHandler = new UserStorageManager();
        this.userDataExtractor = new TwitterUserDataExtractor(driver, navigator, userDataHandler);
        this.tweetDataHandler = new TweetStorageManager();
        this.tweetDataExtractor = new TwitterTweetDataExtractor(driver,navigator, tweetDataHandler);
    }

    public void login(String username, String email, String password) {
        WaitingScene.updateStatus("Logging");

        authenticator.login(username, email, password);
    }

    public void applyFilter(List<String> keywords, int minLikes, int minReplies, int minReposts) {
        WaitingScene.updateStatus("Advance Search Setting");

        filter.advancedSearch(keywords, minLikes, minReplies, minReposts);
    }

    public void navigationalSearchLink(){
        filter.navigateToSearchResultLink();
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

            WaitingScene.updateStatus("Collecting " + user.getProfileLink());
            userDataExtractor.extractData(user.getProfileLink(), 1000, 3);
            userDataHandler.saveData("KOLs.json");
        }
    }

    public void scrapeTweetsData(List<Tweet> tweets) throws IOException {
        for (Tweet tweet : tweets) {
            if (tweet == null) {
                System.out.println("Skipping scrape tweet ");
                continue;

            }
            else {
                System.out.println("Scraping tweet " + tweet.getUserLink());
            }
            tweetDataExtractor.extractData(tweet.getTweetLink(),1000,5);
            tweetDataHandler.saveData("Tweet.json");
        }
    }

    public static void close() {
        driver.close();
    }

    public List<User> getUsers(String filePath) throws IOException {
        return userDataHandler.getUsers(filePath);
    }

    public List<Tweet> getTweets(String filePath) throws IOException {
        return tweetDataHandler.getTweets(filePath);
    }

    private void extractInitialKOLsTo(String filePath) throws IOException {
        System.out.println("Start collecting user data...");

        navigator.clickButton("People");

        List <User> users = userDataExtractor.extractUsers(true, 30, 30);
        for (User user : users) {
            userDataHandler.addUser(filePath, user);
        }

        userDataHandler.saveData(filePath);
    }

    private void extractInitialTweetsTo(String filePath) throws IOException {
        System.out.println("Start collecting tweet data...");

        List <Tweet> tweets = tweetDataExtractor.extractTweets( 15,15);
        for (Tweet tweet : tweets) {
            tweetDataHandler.addTweet(filePath,tweet);
        }

        tweetDataHandler.saveData(filePath);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        controller.login("@nhom_8_OOP","nqkien199hy@gmail.com","kien1992005t1chy");
        controller.applyFilter(
                List.of(args),
                1000,
                1000,
                250);

        controller.extractInitialTweetsTo("Tweet.json");
        List<Tweet> tweets = controller.getTweets("Tweet.json");
        System.out.println("Number of tweets: " + tweets.size());
        controller.scrapeTweetsData(tweets);
        List<Tweet> test1 = controller.getTweets("Tweet.json");

        controller.navigationalSearchLink();

        controller.extractInitialKOLsTo("KOLs.json");

        List<User> users = controller.getUsers("KOLs.json");
        System.out.println("Number of users: " + users.size());
        controller.scrapeUsersData(users);

        List<User> test2 = controller.getUsers("KOLs.json");
        int sum = 0;
        for(User user : test2) {
            System.out.println(sum);
            List<String> list = user.getFollowingList();
            String name = user.getUsername();
            System.out.println("Name: " + name);
            if(list.size() > 3){
                sum++;
            }
        }

        System.out.println("Number of users: " + sum);

        controller.close();
    }
}
