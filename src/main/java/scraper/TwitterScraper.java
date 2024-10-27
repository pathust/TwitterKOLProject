package scraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static model.User.toInt;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TwitterScraper {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navigator navigator;
    private static final int TIMEOUT_SECONDS = 5;
    private static final int MAX_SCROLLS = 2;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ArrayNode userArray = mapper.createArrayNode();

    public TwitterScraper(WebDriver driver, Navigator navigator) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        this.navigator = navigator;
    }

    private List<WebElement> findNextUserCells() {
        return wait.until(presenceOfAllElementsLocatedBy(
                By.xpath("//button[@data-testid='UserCell']")));
    }

    private String getUserProfileLink(WebElement userCell) {
        try {
            return userCell.findElement(
                    By.xpath(".//a[@role='link']")).getAttribute("href");
        } catch (NoSuchElementException e) {
            System.out.println("Unable to find profile link.");
            return "";
        }
    }

    private void addUserToJson(String profileLink, boolean verified) {
        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("profileLink", profileLink);
        userNode.put("verified", verified);
        userArray.add(userNode);
    }

    private void saveJsonToFile(String filename) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), userArray);
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving JSON file: " + e.getMessage());
        }
    }

    private void extractInitialKOLsToJson(String filename) {
        try {
            System.out.println("Start collecting user data...");

            navigator.clickButton("People");
            for (int scrollCount = 0; scrollCount < MAX_SCROLLS; scrollCount++) {
                Thread.sleep(3000);
                List<WebElement> userCells = findNextUserCells();

                if (userCells.isEmpty()) {
                    System.out.println("No user cells found.");
                    break;
                }

                for (int i = 0; i < userCells.size(); i++) {
                    WebElement userCell = wait.until(presenceOfElementLocated(
                            By.xpath("(//button[@data-testid='UserCell'])[" + (i + 1) + "]")));
                    String profileLink = getUserProfileLink(userCell);

                    if (!profileLink.isEmpty()) {
                        addUserToJson(profileLink, true);
                    }
                }
                navigator.scrollDown();
            }

            saveJsonToFile(filename);
        } catch (Exception e) {
            System.out.println("Error processing users: " + e.getMessage());
        }
    }

    public List<String> getUserLinksFromJson(String filePath) {
        List<String> userLinks = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(filePath));

            for (JsonNode userNode : rootNode) {
                String url = userNode.path("profileLink").asText();
                if (!url.isEmpty()) {
                    userLinks.add(url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userLinks;
    }

    private void extractPotentialKOLs() {
        try {
            Thread.sleep(3000);
            List<WebElement> retweets = wait.until(presenceOfAllElementsLocatedBy(By.xpath("//span[@data-testid='socialContext']")));
            System.out.println("Number of retweets found: " + retweets.size());
            if (retweets.isEmpty()) {
                return;
            }

            List<WebElement> potentialKOLs = driver.findElements(By.xpath("//span[@data-testid='socialContext']/following::span[2]"));
            System.out.println("Number of potential KOLs found: " + potentialKOLs.size());

            for (WebElement potentialKOL : potentialKOLs) {
                String kolName = potentialKOL.getText();
                WebElement dateElement = potentialKOL.findElement(By.xpath("following::time[1]"));
                String postedDate = dateElement.getAttribute("datetime");

                System.out.println("Potential KOL: " + kolName);
                System.out.println("Tweet's posted date: " + postedDate);
            }
        } catch (NoSuchElementException e) {
            System.out.println("No reposts found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateAndExtractUserData(String userLink) {
        try {
            driver.get(userLink);
            Thread.sleep(5000);
            WebElement userNameElement = wait.until(presenceOfElementLocated(
                    By.xpath("//div[contains(@data-testid, 'UserName')]//span/span")));
            String username = userNameElement.getText();

            WebElement followingCountElement = wait.until(presenceOfElementLocated(
                    By.xpath("//a[contains(@href, 'following')]//span/span")));
            int followingCount = toInt(followingCountElement.getText());

            WebElement followersCountElement = wait.until(presenceOfElementLocated(
                    By.xpath("//a[contains(@href, 'followers')]//span/span")));
            int followersCount = toInt(followersCountElement.getText());

            System.out.println("User: " + username);
            System.out.println("Following: " + followingCount);
            System.out.println("Followers: " + followersCount);


            extractPotentialKOLs();

        } catch (Exception e) {
            System.out.println("Unable to retrieve data for user at " + userLink);
            e.printStackTrace();
        }
    }

    public void processAllUsersFromJson(String jsonFilePath) {
        List<String> userLinks = getUserLinksFromJson(jsonFilePath);

        System.out.println("Number of userLinks found: " + userLinks.size());

        for (String userLink : userLinks) {
            System.out.println("Processing user: " + userLink);
            navigateAndExtractUserData(userLink);
            try {
                Thread.sleep(5000);  // Wait to avoid rate limits
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void solve() {
        extractInitialKOLsToJson("potential_KOLs.json");
        processAllUsersFromJson("potential_KOLs.json");
    }
}
