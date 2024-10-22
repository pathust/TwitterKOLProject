package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TwitterLogin {
    private WebDriver driver;
    private WebDriverWait wait;

    public TwitterLogin(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String username, String email, String password) {
        try {
            // Open Twitter login page
            driver.get("https://twitter.com/login");

            // Attempt to login until successful or redirected back to the login page
            boolean isLoggedIn = false;
            while (!isLoggedIn) {
                // If Twitter is asking for username/email due to unusual activity
                while (!isPasswordFieldDetected()) {
                    // Wait for the email/username field to be present
                    WebElement validationField = wait.until(presenceOfElementLocated(By.name("text")));
                    validationField.clear();

                    // Determine whether to send an email or username
                    validationField.sendKeys(isEmailField(validationField) ? email : username);

                    // Click "Next" button after entering the username/email
                    WebElement nextButton = wait.until(elementToBeClickable(By.xpath("//span[text()='Next']/..")));
                    nextButton.click();
                }

                // Wait for the password input field to be present and enter the password
                WebElement passwordField = wait.until(presenceOfElementLocated(By.name("password")));
                passwordField.sendKeys(password);

                // Wait for the "Log in" button to be clickable and click it
                WebElement loginButton = wait.until(elementToBeClickable(By.xpath("//span[text()='Log in']/..")));
                loginButton.click();

                // Check for successful login or redirection back to login
                if (driver.getCurrentUrl().contains("twitter.com/home")) {
                    isLoggedIn = true; // Successfully logged in
                } else {
                    // If redirected back to the login page, reset the flow
                    driver.get("https://twitter.com/login");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailField(WebElement field) {
        // Check if the field is for an email address based on the placeholder
        String placeholder = field.getAttribute("placeholder");
        return placeholder != null && placeholder.toLowerCase().contains("email");
    }

    private boolean isPasswordFieldDetected() {
        try {
            // Wait for either the password field or the email/username field to be present
            wait.until(or(
                    presenceOfElementLocated(By.name("password")),
                    presenceOfElementLocated(By.name("text"))
            ));

            // Return true if the password field is found (i.e., the username field is empty)
            return driver.findElements(By.name("text")).isEmpty();
        } catch (Exception e) {
            return true; // Return true if an exception occurs (indicating password field is likely present)
        }
    }
}
