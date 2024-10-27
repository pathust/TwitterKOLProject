package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TwitterFilter {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private static final int TIMEOUT_SECONDS = 5;

    public TwitterFilter(WebDriver driver, Navigator navigator) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        this.navigator = navigator;
    }

    public void searchHashtagWithAdvancedFilters(String hashtag, int minLikes, int minReplies, int minReposts) {
        System.out.println("Start filtering with Advanced Search");
        try {
            WebElement searchBox = wait.until(visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']")));
            searchBox.sendKeys(hashtag);
            searchBox.submit();

            boolean advancedSearchAvailable = false;
            while (!advancedSearchAvailable) {
                try {
                    navigator.clickButton("Advanced search");
                    advancedSearchAvailable = true;
                } catch (Exception e) {
                    System.out.println("Advanced search button not found, reloading the page...");
                    driver.navigate().refresh();
                }
            }

            try {
                WebElement advancedSearchForm = wait.until(visibilityOfElementLocated(
                        By.xpath("//div[@aria-labelledby='modal-header']")));
                if (advancedSearchForm != null) {
                    System.out.println("Advanced search form found!");

                    fillAdvancedSearchForm(hashtag, minLikes, minReplies, minReposts);
                }
            } catch (Exception e) {
                System.out.println("Advanced search form not found.");
                return;
            }

            try {
                navigator.clickButton("Search");
            } catch (Exception e) {
                System.out.println("Search button not found: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillAdvancedSearchForm(String hashtag, int minReplies, int minLikes, int minReposts) {
        try {

            navigator.fillingFieldBySpan("All of these words", hashtag);
            navigator.fillingFieldBySpan("Minimum replies", String.valueOf(minReplies));
            navigator.fillingFieldBySpan("Minimum Likes", String.valueOf(minLikes));
            navigator.fillingFieldBySpan("Minimum reposts", String.valueOf(minReposts));
        } catch (Exception e) {
            System.out.println("Error filling advanced search form: " + e.getMessage());
        }
    }

}
