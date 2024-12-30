import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReformatAndNavigate {
    public static void main(String[] args) {
        // Set the path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        // Path to the .txt file containing the original links
        String filePath = "path/to/links.txt";

        // Base URL for reformatting
        String baseURL = "https://livecounts.io/twitter-live-follower-counter/";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String originalLink;
            while ((originalLink = br.readLine()) != null) {
                // Trim whitespace
                originalLink = originalLink.trim();

                // Skip empty lines
                if (!originalLink.isEmpty()) {
                    // Extract the username from the original link
                    String username = originalLink.substring(originalLink.lastIndexOf("/") + 1);

                    // Reformat the link
                    String reformattedLink = baseURL + username;

                    // Print the reformatted link (for debugging)
                    System.out.println("Navigating to: " + reformattedLink);

                    // Navigate to the reformatted link
                    driver.get(reformattedLink);

                    // Wait for 3 seconds (adjustable)
                    Thread.sleep(3000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}