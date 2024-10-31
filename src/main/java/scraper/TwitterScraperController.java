package scraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
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

import java.io.File;
import java.io.IOException;
import java.util.List;


public class TwitterScraperController {
    private final WebDriver driver;
    private final Navigator navigator;
    private final Authenticator authenticator;
    private final Filter filter;
    private final UserDataExtractor userDataExtractor;
    private final UserDataHandler userDataHandler;

    public TwitterScraperController() {
        System.setProperty(
                "webdriver.chrome.driver",
                "D:\\Dowload\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        this.driver = new ChromeDriver();
        this.navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = new TwitterFilter(driver, navigator);
        this.userDataHandler = new UserStorageManager();
        this.userDataExtractor = new TwitterUserDataExtractor(driver, navigator, userDataHandler);
    }

    public void login(String username, String email, String password) {
        authenticator.login(username, email, password);
    }

    public void applyFilter(List<String> keywords, int minLikes, int minReplies, int minReposts) {
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
            userDataExtractor.extractData(user.getProfileLink(), 10);
            userDataHandler.saveData("KOLs.json");
        }
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    public List<User> getUsers(String filePath) throws IOException {
        return userDataHandler.getUsers(filePath);
    }

    private void extractInitialKOLsTo(String filePath) throws IOException {
        System.out.println("Start collecting user data...");

        navigator.clickButton("People");

        List <User> users = userDataExtractor.extractUsers(true, 30);
        for (User user : users) {
            userDataHandler.addUser(filePath, user);
        }

        userDataHandler.saveData(filePath);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        controller.login(
                "@DThank05",
                "dinhthanh020105@gmail.com",
                "xuanthanh123");

        controller.applyFilter(
                List.of(args[0]),
                10000,
                1000,
                200);

        controller.extractInitialKOLsTo("KOLs.json");

        List<User> users = controller.getUsers("KOLs.json");
        System.out.println("Number of users: " + users.size());
        controller.scrapeUsersData(users);

        controller.close();
    }
}
