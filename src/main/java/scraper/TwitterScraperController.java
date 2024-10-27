package scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;


public class TwitterScraperController {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver");
        WebDriver driver = new ChromeDriver();
        Navigator navigator = new Navigator(driver);

        TwitterLogin login = new TwitterLogin(driver, navigator);
        login.login("@21Oop36301", "penaldomessy21@gmail.com", "123456789@21oop");

        TwitterFilter filter = new TwitterFilter(driver, navigator);
        filter.searchHashtagWithAdvancedFilters("#blockchain", 1000, 500, 200);
        System.out.println("Done");

        TwitterScraper scraper = new TwitterScraper(driver, navigator);
        scraper.scrape();
        System.out.println("Done");
//        driver.quit();
    }
}
