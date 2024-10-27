package scraper;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class Navigator {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final int TIMEOUT_SECONDS = 5;

    public Navigator(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void clickButton(String buttonName) {
        WebElement button = wait.until(elementToBeClickable(
                By.xpath("//span[text()='" + buttonName + "']/..")));
        button.click();
    }

    public void fillingFieldBySpan(String spanText, String text) {
        WebElement field = wait.until(presenceOfElementLocated(
                By.xpath("//label[div/div/div/span[text()='" + spanText + "']]//div[2]/div/input")));
        scrollToElement(field);
        field.clear();
        field.sendKeys(text);
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
