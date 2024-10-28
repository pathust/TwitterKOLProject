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
import scraper.storage.DataHandler;
import scraper.storage.UserStorageManager;

import java.io.IOException;
import java.util.List;


public class TwitterScraperController {
    private final WebDriver driver;
    private final Navigator navigator;
    private final Authenticator authenticator;
    private final Filter filter;
    private final UserDataExtractor userDataExtractor;
    private DataHandler dataHandler;

    public TwitterScraperController() throws IOException {
        System.setProperty(
                "webdriver.chrome.driver",
                "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver");
        this.driver = new ChromeDriver();
        this.navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = (Filter) new TwitterFilter(driver, navigator);
        this.dataHandler = new UserStorageManager();
        this.userDataExtractor = new TwitterUserDataExtractor(driver, navigator, dataHandler);
    }

    public void login(String username, String email, String password) {
        authenticator.login(username, email, password);
    }

    public void applyFilter(List<String> keywords, int maxFollowers, int minFollowers, int maxTweets) {
        filter.advancedSearch(keywords, maxFollowers, minFollowers, maxTweets);
    }

    public void scrapeUserData(List<String> userLinks) throws InterruptedException {
        for (String userLink : userLinks) {
            userDataExtractor.extractData(userLink);
        }
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    public List<String> getUserLinksFrom(String filePath) throws IOException {
        return dataHandler.getUserLinksFrom(filePath);
    }

    private void extractInitialKOLsTo(String filePath) throws InterruptedException, IOException {
        System.out.println("Start collecting user data...");

        navigator.clickButton("People");

        userDataExtractor.extractUserTo(filePath, true);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        controller.login(
                "@21Oop36301",
                "penaldomessy21@gmail.com",
                "123456789@21oop");

        controller.applyFilter(
                List.of("blockchain"),
                10000,
                1000,
                200);

        controller.extractInitialKOLsTo("KOLs.json");

        List<String> userLinks = controller.getUserLinksFrom("KOLs.json");
        System.out.println("Number of user links: " + userLinks.size());
        controller.scrapeUserData(userLinks);

        controller.close();
    }
}
