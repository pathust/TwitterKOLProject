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

    public TwitterScraper() {
        String chromedriverPath = "/Users/phananhtai/Downloads/chromedriver-mac-arm64/chromedriver";
        System.setProperty("webdriver.chrome.driver",
                chromedriverPath);
        this.driver = new ChromeDriver();
    }

    public void login(String username, String password) {
        try {
            // Open Twitter login page
            driver.get("https://twitter.com/login");
            Thread.sleep(10000); // Sleep after opening the page

            // Enter username
            driver.findElement(By.name("text")).sendKeys("@21Oop36301");
            Thread.sleep(1000); // Sleep after entering username

            // Click on "Next" button
            driver.findElement(By.xpath("//span[text()='Next']/..")).click();
            Thread.sleep(2000); // Sleep after clicking next

            // Enter password
            driver.findElement(By.name("password")).sendKeys("123456789@21oop");
            Thread.sleep(1000); // Sleep after entering password

            // Click on "Log in" button
            driver.findElement(By.xpath("//span[text()='Log in']/..")).click();
            Thread.sleep(2000); // Sleep after logging in
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}