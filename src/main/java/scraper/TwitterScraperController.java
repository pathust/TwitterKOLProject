package scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class TwitterScraperController {
    public static void main(String[] args) {
        String chromedriverPath = "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        WebDriver driver = new ChromeDriver();

        TwitterLogin login = new TwitterLogin(driver);
        login.login("@21Oop36301", "penaldomessy21@gmail.com", "123456789@21oop");

        TwitterScraper scraper = new TwitterScraper(driver);
        List<String> trends = scraper.getTrendingKOLsAndHashtags();
        System.out.println("Trending Topics: " + trends);

        driver.quit();
    }
}
