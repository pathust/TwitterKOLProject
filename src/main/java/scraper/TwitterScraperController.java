package scraper;

import UI.waiting.WaitingScene;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import scraper.authentication.Authenticator;
import scraper.authentication.TwitterAuthenticator;
import scraper.extractor.ExtractorController;
import scraper.filtering.Filter;
import scraper.filtering.TwitterFilter;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;
import storage.StorageHandler;


import java.io.IOException;
import java.util.List;

import static utils.ObjectType.TWEET;
import static utils.ObjectType.USER;

public class TwitterScraperController {
    private static WebDriver driver;
    private final Authenticator authenticator;
    private final Filter filter;
    private final ExtractorController extractorController;
    private final StorageHandler storageHandler;

    public TwitterScraperController() {
        System.setProperty(
                "webdriver.chrome.driver",
                "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();
        Navigator navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = new TwitterFilter(driver, navigator);
        this.extractorController = new ExtractorController(driver);
        this.storageHandler = new StorageHandler();
    }

    public void login(String username, String email, String password) {
        WaitingScene.updateStatus("Logging");
        authenticator.login(username, email, password);
    }

    public void applyFilter(List<String> keywords, int minLikes, int minReplies, int minReposts) {
        WaitingScene.updateStatus("Advance Search Setting");
        filter.advancedSearch(keywords, minLikes, minReplies, minReposts);
    }

    public static void close() {
        driver.close();
    }

    public void extractData() throws IOException, InterruptedException {
        extractorController.extractData();
    }

    public void saveData() throws IOException, InterruptedException {
        this.storageHandler.save(USER, "KOLs");
        this.storageHandler.save(TWEET, "Tweet");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        //controller.login("@21Oop36301","penaldomessy21@gmail.com","123456789@21oop");
        controller.login("@nhom_8_OOP","nqkien199hy@gmail.com","kien1992005t1chy");
        controller.applyFilter(
                List.of(args),
                1000,
                1000,
                250);
        try {
            controller.extractData();
        }
        catch (Exception e) {
            controller.saveData();
            System.out.println("Haven't completed!");
        }
        close();
    }
}
