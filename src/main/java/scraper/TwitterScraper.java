package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;

public class TwitterScraper {
    private WebDriver driver;
    private WebDriverWait wait;

    public TwitterScraper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<String> getTrendingKOLsAndHashtags() {
        List<String> trendingInfo = new ArrayList<>();
        try {
            // Navigate to the homepage or trends page if needed
            driver.get("https://twitter.com/home");

            // Wait for the trends section to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Locate the trends section using the provided selector
            List<WebElement> trends = wait.until(visibilityOfAllElementsLocatedBy(By.cssSelector("#react-root > div > div > div.css-175oi2r.r-1f2l425.r-13qz1uu.r-417010.r-18u37iz > main > div > div > div > div.css-175oi2r.r-aqfbo4.r-1l8l4mf.r-1jocfgc > div > div.css-175oi2r.r-1jocfgc.r-gtdqiz > div > div > div > div:nth-child(4) > div > section > div > div")));

            for (WebElement trend : trends) {
                trendingInfo.add(trend.getText()); // Get text from each trend element
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trendingInfo;
    }

    // Optionally, add a method to extract KOLs based on trends
    public List<String> getKOLsFromTrends(List<String> trends) {
        List<String> kolList = new ArrayList<>();
        for (String trend : trends) {
            System.out.println(trend);
        }
        return kolList;
    }
}
