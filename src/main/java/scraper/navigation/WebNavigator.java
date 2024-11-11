package scraper.navigation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class WebNavigator implements Navigator {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public WebNavigator(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void clickButton(WebElement element, String buttonName) {
        String xpathExpression = ".//button//span[text()='" + buttonName + "']/..";
        WebElement button;
        int maxRetries = 10;
        int retryCount = 0;
        boolean clicked = false;

        while (!clicked && retryCount < maxRetries) {
            try {
                if (element == null) {
                    button = driver.findElement(By.xpath(xpathExpression));
                } else {
                    button = element.findElement(By.xpath(xpathExpression));
                }

                // Attempt to click the button
                button.click();
                clicked = true; // Exit loop if click is successful
            } catch (Exception e) {
                System.out.println("Attempt " + (retryCount + 1) + ": Could not find or click button " + buttonName);
                retryCount++;
                try {
                    Thread.sleep(100); // Short pause before retrying (100 ms)
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (!clicked) {
            System.out.println("Failed to click button " + buttonName + " after " + maxRetries + " attempts.");
        }
    }

    public void fillingFieldBySpan(String spanText, String text) {
        WebElement field = wait.until(presenceOfElementLocated(
                By.xpath("//label[div/div/div/span[text()='" + spanText + "']]//div[2]/div/input")));
        scrollToElement(field);
        field.clear();
        field.sendKeys(text);
    }

    public String getLink(WebElement element) {
        return element.findElement(
                By.xpath(".//a[@role='link']")).getAttribute("href");
    }

    public void navigateToSection(String section) {
        if (section.isEmpty()) {
            System.err.println("No section found");
            return;
        }
        WebElement sectionLink = wait.until(presenceOfElementLocated(
                By.xpath("//a[contains(@href, '" + section + "')]")));
        sectionLink.click();
    }
}
