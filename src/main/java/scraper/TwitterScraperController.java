package scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TwitterScraperController {
    public static void main(String[] args) {
        String chromedriverPath = "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        WebDriver driver = new ChromeDriver();

        TwitterLogin login = new TwitterLogin(driver);
        login.login("PogbaPaul432283", "anhrooneymtp@gmail.com", "anhmanunited");

        TwitterFilter filter = new TwitterFilter(driver);
        filter.searchHashtagWithAdvancedFilters("#blockchain", 1000, 500, 200);
        System.out.println("Done");

        TwitterScraper scraper = new TwitterScraper(driver);
        scraper.processSearchResults();
        System.out.println("Done");
//        driver.quit();
    }
}
