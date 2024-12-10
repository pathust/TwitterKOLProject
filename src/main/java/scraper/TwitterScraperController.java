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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static utils.ObjectType.TWEET;
import static utils.ObjectType.USER;

public class TwitterScraperController {
    private static WebDriver driver;
    private final Navigator navigator;
    private final Authenticator authenticator;
    private final Filter filter;
    private final ExtractorController extractorController;
    private final StorageHandler storageHandler;
    private final ScheduledExecutorService scheduler;

    public TwitterScraperController() throws IOException {
        System.setProperty(
                "webdriver.chrome.driver",
                "D:\\Dowload\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        this.navigator = new WebNavigator(driver);
        this.authenticator = new TwitterAuthenticator(driver, navigator);
        this.filter = new TwitterFilter(driver, navigator);
        this.storageHandler = new StorageHandler();
        this.extractorController = new ExtractorController(driver, navigator, storageHandler);


//        this.storageHandler.load(USER, "KOLs");
//        this.storageHandler.load(TWEET, "Tweets");

        System.out.println("loaded");

        this.scheduler = Executors.newScheduledThreadPool(1);
        schedulePeriodicSave();
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

    public void saveData() throws IOException {
        this.storageHandler.save(USER, "KOLs");
        this.storageHandler.save(TWEET, "Tweet");
    }

    public void schedulePeriodicSave() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("Periodic save started...");
                saveData();
                System.out.println("Periodic save completed.");
            } catch (IOException e) {
                System.err.println("Error during periodic save: " + e.getMessage());
            }
        }, 5, 5, TimeUnit.SECONDS); // Delay lần đầu là 30s, lặp lại mỗi 30s
    }

    public void stopScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Scheduler did not terminate in time. Forcing shutdown.");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TwitterScraperController controller = new TwitterScraperController();

        controller.login("@21Oop36301","penaldomessy21@gmail.com","123456789@21oop");
//        controller.login("@nhom_8_OOP","nqkien199hy@gmail.com","kien1992005t1chy");
        controller.applyFilter(
                List.of(args),
                1000,
                1000,
                250);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Saving data before exiting...");
                controller.saveData();
                controller.stopScheduler(); // Dừng scheduler khi chương trình kết thúc
                System.out.println("Data saved successfully.");
            } catch (IOException e) {
                System.err.println("Error saving data: " + e.getMessage());
            }
        }));

        try {
            controller.extractData();
        }
        catch (Exception e) {
            System.out.println("Haven't completed!");
        }

        controller.saveData();
        close();
    }
}
