package scraper;

import model.User;
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
    private static final int TIMEOUT_SECONDS = 5; // Increased timeout for better reliability

    public TwitterFilter(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    public void searchHashtagWithAdvancedFilters(String hashtag, int minLikes, int minReplies, int minReposts) {
        System.out.println("Start filtering with Advanced Search");
        try {
            // Step 1: Wait for the search box to appear and search for the hashtag
            WebElement searchBox = wait.until(visibilityOfElementLocated(By.xpath("//input[@placeholder='Search']")));
            searchBox.sendKeys(hashtag);
            searchBox.submit();

            // Step 2: Check for the Advanced Search button
            boolean advancedSearchAvailable = false;
            while (!advancedSearchAvailable) {
                try {
                    WebElement advancedSearchButton = wait.until(elementToBeClickable(By.xpath("//span[text()='Advanced search']")));
                    advancedSearchButton.click();
                    advancedSearchAvailable = true;
                } catch (Exception e) {
                    System.out.println("Advanced search button not found, reloading the page...");
                    driver.navigate().refresh();
                }
            }

            // Step 3: Wait for the advanced search form to be visible
            try {
                WebElement advancedSearchForm = wait.until(visibilityOfElementLocated(
                        By.xpath("//div[@aria-labelledby='modal-header']")));
                if (advancedSearchForm != null) {
                    System.out.println("Advanced search form found!");

                    // Fill the form with values
                    fillAdvancedSearchForm(hashtag, minLikes, minReplies, minReposts);
                }
            } catch (Exception e) {
                System.out.println("Advanced search form not found.");
                return; // Exit the method if form is not found
            }

            // Step 6: Click the "Search" button (if present)
            try {
                WebElement searchButton = wait.until(elementToBeClickable(
                        By.xpath("//button[.//span[text()='Search']]")));
                searchButton.click();
            } catch (Exception e) {
                System.out.println("Search button not found: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void fillAdvancedSearchForm(String hashtag, int minReplies, int minLikes, int minReposts) {
        try {

            fillingFieldBySpan("All of these words", hashtag);
            fillingFieldBySpan("Minimum replies", String.valueOf(minReplies));
            fillingFieldBySpan("Minimum Likes", String.valueOf(minLikes));
            fillingFieldBySpan("Minimum reposts", String.valueOf(minReposts));
        } catch (Exception e) {
            System.out.println("Error filling advanced search form: " + e.getMessage());
        }
    }

    private void fillingFieldBySpan(String spanText, String text) {
        WebElement field = wait.until(presenceOfElementLocated(
                By.xpath("//label[div/div/div/span[text()='" + spanText + "']]//div[2]/div/input")));
        scrollToElement(field); // Scroll to the Minimum reposts field
        field.clear();
        field.sendKeys(text);
    }

}
