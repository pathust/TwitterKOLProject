package scraper.navigation;

import org.openqa.selenium.*;
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

    public void scrollBy(int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
    }

    @Override
    public void clickButton(String parentXPath, String buttonName) {
        String xpathExpression = parentXPath + "//button//span[text()='" + buttonName + "']"; // Scoped to element

        try {
            WebElement button;
            button = driver.findElement(By.xpath(xpathExpression));

            Thread.sleep(1000);
            if (button == null) {
                System.out.println("Button not found");
            }
            button = wait.until(elementToBeClickable(button));
            button.click();
        }
        catch (Exception e) {
            System.out.println("Followed");
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
                By.xpath("//a[@role='link']")).getAttribute("href");
    }

    public void navigateToSection(String section) {
        if (section.isEmpty()) {
            System.err.println("No section found");
            return;
        }
        String sectionLink = wait.until(
                presenceOfElementLocated(
                        By.xpath("//a[contains(@href, '" + section + "')]")
                )).getAttribute("href");
        while (!driver.getCurrentUrl().contains(section)) {
            driver.navigate().to(sectionLink);
        }
    }
}
