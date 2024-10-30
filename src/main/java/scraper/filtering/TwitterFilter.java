package scraper.filtering;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import scraper.navigation.WebNavigator;

import javax.annotation.processing.Filer;
import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TwitterFilter implements Filter {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;

    public TwitterFilter(WebDriver driver, Navigator navigator) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
    }

    @Override
    public void advancedSearch(List<String> words, int minLikes, int minReplies, int minReposts) {
        try {
            System.out.println("Start filtering with Advanced Search");
            WebElement searchBox = wait.until(presenceOfElementLocated(
                    By.xpath("//input[@placeholder='Search']")));
            System.out.println(searchBox.getText());
            String wordsKey = String.join(" ", words);
            searchBox.sendKeys(wordsKey);
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

                    fillAdvancedSearchForm(wordsKey, minLikes, minReplies, minReposts);
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
        System.out.println("Finish filtering with Advanced Search");
    }

    private void fillAdvancedSearchForm(String wordsKey, int minReplies, int minLikes, int minReposts) {
        try {

            navigator.fillingFieldBySpan("All of these words", wordsKey);
            navigator.fillingFieldBySpan("Minimum replies", String.valueOf(minReplies));
            navigator.fillingFieldBySpan("Minimum Likes", String.valueOf(minLikes));
            navigator.fillingFieldBySpan("Minimum reposts", String.valueOf(minReposts));
        } catch (Exception e) {
            System.out.println("Error filling advanced search form: " + e.getMessage());
        }
    }

}
