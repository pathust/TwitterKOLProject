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

    @Override
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @Override
    public void scrollToElement(WebElement element, int delay) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        if (delay != 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    @Override
    public void clickButton(WebElement element, String buttonName) {
        String scopedXPath = ".//button//span[text()='" + buttonName + "']"; // Scoped to element
        String globalXPath = "//button//span[text()='" + buttonName + "']";

        try {
            WebElement button;
            if (element == null) {
                button = driver.findElement(By.xpath(globalXPath));
            }
            else {
                button = element.findElement(By.xpath(globalXPath));
            }

            Thread.sleep(1000);
            button = wait.until(elementToBeClickable(button));
            button.click();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void fillingFieldBySpan(String spanText, String text) {
        WebElement field = wait.until(presenceOfElementLocated(
                By.xpath("//label[div/div/div/span[text()='" + spanText + "']]//div[2]/div/input")));
        scrollToElement(field);
        field.clear();
        field.sendKeys(text);
    }

    @Override
    public String getLink(WebElement element) {
        return element.findElement(
                By.xpath(".//a[@role='link']")).getAttribute("href");
    }

    @Override
    public void navigateToSection(String section) {
        if (section.isEmpty()) {
            System.err.println("No section found");
            return;
        }
        WebElement sectionLink = wait.until(elementToBeClickable(
                By.xpath("//a[contains(@href, '" + section + "')]")));
        sectionLink.click();
    }
}
