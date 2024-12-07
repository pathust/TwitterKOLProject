package scraper.authentication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TwitterAuthenticator implements Authenticator {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private WebElement label;

    public TwitterAuthenticator(WebDriver driver, Navigator navigator) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.navigator = navigator;
    }

    @Override
    public void login(String username, String email, String password) {
        try {
            System.out.println("Logging in...");
            driver.get("https://twitter.com/login");

            boolean isLoggedIn = false;
            while (!isLoggedIn) {
                while (!isPasswordField()) {
                    try {
                        if (isEmailField()) {
                            sendKeys(email);
                        } else {
                            sendKeys(username);
                        }

                        navigator.clickButton("","Next");
                    } catch (Exception e) {
                        System.err.println("Error entering username or email.");
                        e.printStackTrace();
                    }
                }

                try {
                    sendKeys(password);
                    navigator.clickButton("","Log in");

                    Thread.sleep(5000);
                    if (driver.getCurrentUrl().contains("home")) {
                        isLoggedIn = true;
                    }
                    else {
                        System.out.println(driver.getCurrentUrl());
                    }
                } catch (Exception e) {
                    System.err.println("Error during password entry or login.");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error during login process.");
            e.printStackTrace();
        }

        System.out.println("Login process completed.");
    }

    private void sendKeys(String keysToSend) {
        try {
            WebElement field = label.findElement(By.xpath("(//input)[last()]"));
            System.out.println(keysToSend);
            field.sendKeys(keysToSend);
        } catch (Exception e) {
            System.err.println("Error sending key: " + keysToSend);
            e.printStackTrace();
        }
    }

    private boolean isEmailField() {
        return !label.findElements(By.xpath("//span[contains(text(), 'mail')]")).isEmpty();
    }

    private boolean isPasswordField() {
        try {
            label = wait.until(presenceOfElementLocated(By.xpath("//label")));
            return label.findElements(By.name("text")).isEmpty();
        } catch (Exception e) {
            System.err.println("Error finding label element.");
            e.printStackTrace();
            return false;
        }
    }
}
