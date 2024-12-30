package scraper.filtering;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;

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
            WebElement searchBox = wait.until(presenceOfElementLocated(
                    By.xpath("//input[@placeholder='Search']")));
            String wordsKey = String.join(" ", words);
            searchBox.sendKeys(wordsKey);
            searchBox.submit();

            boolean advancedSearchAvailable = false;
            while (!advancedSearchAvailable) {
                try {
                    navigator.navigateToSection("search-advanced");
                    advancedSearchAvailable = true;
                } catch (Exception e) {
                    driver.navigate().refresh();
                }
            }

            try {
                WebElement advancedSearchForm = wait.until(visibilityOfElementLocated(
                        By.xpath("//div[@aria-labelledby='modal-header']")));
                if (advancedSearchForm != null) {

                    fillAdvancedSearchForm(wordsKey, minLikes, minReplies, minReposts);
                }
            } catch (Exception e) {
                return;
            }

            navigator.clickButton("", "Search");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillAdvancedSearchForm(String wordsKey, int minReplies, int minLikes, int minReposts) {
        navigator.fillingFieldBySpan("Any of these words", wordsKey);
        navigator.fillingFieldBySpan("Minimum replies", String.valueOf(minReplies));
        navigator.fillingFieldBySpan("Minimum Likes", String.valueOf(minLikes));
        navigator.fillingFieldBySpan("Minimum reposts", String.valueOf(minReposts));
    }
}
