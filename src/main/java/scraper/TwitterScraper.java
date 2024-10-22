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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait for 10 seconds max
    }

    public void login(String username, String email, String password) {
        try {
            // Open Twitter login page
            driver.get("https://twitter.com/login");

            // Wait for the username input field to be present and enter the username
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("text")));
            usernameField.sendKeys(username);

            // Wait for the "Next" button to be clickable and click it
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']/..")));
            nextButton.click();

            // Wait for the password field or the validation step for unusual activity
            boolean unusualLoginDetected = waitForEmailOrPasswordField();

            // If Twitter is asking for username/email due to unusual activity
            if (unusualLoginDetected) {
                // Check for the email/username field and enter the appropriate value
                WebElement validationField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("text")));
                validationField.clear();
                validationField.sendKeys(email != null && !email.isEmpty() ? email : username);

                // Click "Next" after entering the username/email
                WebElement emailNextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']/..")));
                emailNextButton.click();
            }

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

    private boolean waitForEmailOrPasswordField() {
        try {
            // Check if the email/username field appears before the password field
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.name("password")),
                    ExpectedConditions.presenceOfElementLocated(By.name("text"))
            ));

            // Return true if the email/username field is found
            return !driver.findElements(By.name("text")).isEmpty();
        } catch (Exception e) {
            return false; // Return false if it times out or if an exception occurs
        }
    }
}
