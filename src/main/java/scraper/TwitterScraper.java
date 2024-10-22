package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class TwitterScraper {
    private WebDriver driver;
    private WebDriverWait wait;

    public TwitterScraper() {
        String chromedriverPath = "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        this.driver = new ChromeDriver();
        // Initialize WebDriverWait with a timeout of 10 seconds
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void login(String username, String password) {
        try {
            // Open Twitter login page
            driver.get("https://twitter.com/login");

            // Wait for the username input field to be present and enter the username
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("text")));
            usernameField.sendKeys(username);

            // Wait for the "Next" button to be clickable and click it
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']/..")));
            nextButton.click();

            // Wait for the password input field to be present and enter the password
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
            passwordField.sendKeys(password);

            // Wait for the "Log in" button to be clickable and click it
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Log in']/..")));
            loginButton.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
