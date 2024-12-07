package scraper;

import UI.waiting.WaitingScene;
import model.User;
import model.Tweet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import scraper.authentication.Authenticator;
import scraper.authentication.TwitterAuthenticator;
import scraper.extractor.UserDataExtractor;
import scraper.extractor.TweetDataExtractor;
import scraper.extractor.Extractor;
import scraper.filtering.Filter;
import scraper.filtering.TwitterFilter;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;
import storage.DataRepository;
import storage.StorageHandler;

import java.io.IOException;
import java.util.List;

import static utils.ObjectType.TWEET;
import static utils.ObjectType.USER;


public class TwitterScraperController {
    private static WebDriver driver;
    private final Navigator navigator;
    private final Authenticator authenticator;
    private final Filter filter;
    private final DataRepository dataRepository;
    private final Extractor<User> userDataExtractor;
    private final Extractor<Tweet> tweetDataExtractor;

    public TwitterScraperController() {
        System.setProperty(
                "webdriver.chrome.driver",
                "C://Users//pc11w//Downloads//chromedriver-win64//chromedriver-win64//chromedriver.exe");
        driver = new ChromeDriver();
        this.navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = new TwitterFilter(driver, navigator);
        this.dataRepository = new StorageHandler();
        this.userDataExtractor = new UserDataExtractor(driver, navigator, dataRepository) {};
        this.tweetDataExtractor = new TweetDataExtractor(driver, navigator, dataRepository);
    }

    public void login(String username, String email, String password) {
        WaitingScene.updateStatus("Logging");

        authenticator.login(username, email, password);
    }

    public void applyFilter(List<String> keywords, int minLikes, int minReplies, int minReposts) {
        WaitingScene.updateStatus("Advance Search Setting");

        filter.advancedSearch(keywords, minLikes, minReplies, minReposts);
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
            userDataExtractor.extractData(user.getProfileLink());
            dataRepository.save(USER,"KOLs.json");
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
            dataRepository.save(TWEET, "Tweet.json");
        }
    }

    public static void close() {
        driver.close();
    }

    public List<User> getUsers(String filePath) throws IOException {
        return dataRepository.getAll(USER, filePath)
                .stream()
                .filter(item -> item instanceof User)
                .map(item -> (User) item)
                .toList();
    }

    public List<Tweet> getTweets(String filePath) throws IOException {
        return dataRepository.getAll(TWEET, filePath)
                .stream()
                .filter(item -> item instanceof User)
                .map(item -> (Tweet) item)
                .toList();
    }

    private void extractInitialKOLsTo(String filePath) throws IOException {
        System.out.println("Start collecting user data...");

        navigator.navigateToSection("user");

        List <User> users = userDataExtractor.extractItems(10, true);
        for (User user : users) {
            System.out.println("User " + user.getUsername());
            dataRepository.add(USER, filePath, user);
        }

        dataRepository.save(USER, filePath);
    }

    private void extractInitialTweetsTo(String filePath) throws IOException {
        System.out.println("Start collecting tweet data...");

        List <Tweet> tweets = tweetDataExtractor.extractItems(5, true);
        for (Tweet tweet : tweets) {
            dataRepository.add(TWEET, filePath,tweet);
        }

        dataRepository.save(TWEET, filePath);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        controller.login("@21Oop36301","penaldomessy21@gmail.com","123456789@21oop");
        controller.applyFilter(
                List.of(args),
                1000,
                1000,
                250);

        String searchResultLink = driver.getCurrentUrl();
        // Extract data from tweets
        controller.extractInitialTweetsTo("Tweet.json");
        List<Tweet> tweets = controller.getTweets("Tweet.json");
        System.out.println("Number of tweets: " + tweets.size());
        controller.scrapeTweetsData(tweets);

        while (!driver.getCurrentUrl().contains("search")) {
            driver.get(searchResultLink);
        }

        // Extract data from users
        controller.extractInitialKOLsTo("KOLs.json");
        List<User> users = controller.getUsers("KOLs.json");
        System.out.println("Number of users: " + users.size());
        controller.scrapeUsersData(users);

        driver.quit();
    }
}
