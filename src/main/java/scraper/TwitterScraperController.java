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

import java.io.IOException;
import java.util.List;

public class TwitterScraperController {
    private static WebDriver driver;
    private final Navigator navigator;
    private final Authenticator authenticator;
    private final Filter filter;
    private final ExtractorController extractorController;

    public TwitterScraperController() {
        System.setProperty("webdriver.chrome.driver", "D:\\Selenium\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        this.navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = new TwitterFilter(driver, navigator);
        this.extractorController = new ExtractorController(driver);

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

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        controller.login("@21Oop36301","penaldomessy21@gmail.com","123456789@21oop");
        controller.applyFilter(
                List.of(args),
                1000,
                1000,
                250);
        controller.extractData();
        driver.quit();
    }
}
