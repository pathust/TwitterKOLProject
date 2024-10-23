package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TwitterScraper {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final int TIMEOUT_SECONDS = 1; // Increased timeout for better reliability

    public TwitterScraper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    public void processSearchResults() {
        try {
            System.out.println("Start crawling data ...");
            // Wait for UserCell
            Thread.sleep(3000);
            List<WebElement> userCells = wait.until(presenceOfAllElementsLocatedBy(By.xpath("//button[@data-testid='UserCell']")));

            // Check if any user cells were found
            if (userCells.isEmpty()) {
                System.out.println("No user cells found.");
                return; // Exit if no user cells are found
            }

            for (WebElement userCell : userCells) {
                List<WebElement> leafSpans = userCell.findElements(
                        By.xpath(".//span[not(*)]"));

                // Step 3: Iterate over the leaf spans and print their text content
                for (WebElement leafSpan : leafSpans) {
                    if (!leafSpan.getText().isEmpty()) {
                        System.out.println(leafSpan.getText());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing search results: " + e.getMessage());
        }
    }
}
