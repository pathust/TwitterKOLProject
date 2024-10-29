package scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import scraper.authentication.Authenticator;
import scraper.authentication.TwitterAuthenticator;
import scraper.extractor.TwitterUserDataExtractor;
import scraper.extractor.UserDataExtractor;
import scraper.filtering.Filter;
import scraper.filtering.TwitterFilter;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;
import scraper.storage.UserDataHandler;
import scraper.storage.UserStorageManager;

import java.io.IOException;
import java.util.List;


public class TwitterScraperController {
    private final WebDriver driver;
    private final Navigator navigator;
    private final Authenticator authenticator;
    private final Filter filter;
    private final UserDataExtractor userDataExtractor;
    private UserDataHandler userDataHandler;

    public TwitterScraperController() throws IOException {
        System.setProperty(
                "webdriver.chrome.driver",
                "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver");
        this.driver = new ChromeDriver();
        this.navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = (Filter) new TwitterFilter(driver, navigator);
        this.userDataHandler = new UserStorageManager();
        this.userDataExtractor = new TwitterUserDataExtractor(driver, navigator, userDataHandler);
    }

    public void login(String username, String email, String password) {
        authenticator.login(username, email, password);
    }

    public void applyFilter(List<String> keywords, int maxFollowers, int minFollowers, int maxTweets) {
        filter.advancedSearch(keywords, maxFollowers, minFollowers, maxTweets);
    }

    public void scrapeUsersData(List<String> userLinks) throws InterruptedException, IOException {
        for (String userLink : userLinks) {
            userDataExtractor.extractData(userLink);
        }
        userDataHandler.saveData("KOLs.json");
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    public List<String> getUserLinksFrom(String filePath) throws IOException {
        return userDataHandler.getUserLinksFrom(filePath);
    }

    private void extractInitialKOLsTo(String filePath) throws InterruptedException, IOException {
        System.out.println("Start collecting user data...");

        navigator.clickButton("People");

        userDataExtractor.extractUserTo(filePath, true);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        controller.login(
                "@PogbaPaul432283",
                "anhrooneymtp@gmail.com",
                "anhmanunited");

        controller.applyFilter(
                List.of("blockchain"),
                10000,
                1000,
                200);

        controller.extractInitialKOLsTo("KOLs.json");

        List<String> userLinks = controller.getUserLinksFrom("KOLs.json");
        System.out.println("Number of user links: " + userLinks.size());
        controller.scrapeUsersData(userLinks);

        controller.close();
    }
}
