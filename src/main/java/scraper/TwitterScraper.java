package scraper;

import model.KOL;
import model.Tweet;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.time.temporal.ChronoUnit;


import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TwitterScraper {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final int TIMEOUT_SECONDS = 5;

    public TwitterScraper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    List<KOL> listOfPotentialKOL = new ArrayList<>();

    public void processSearchResults() {
        try {
            driver.get("https://x.com/search?q=%23blockchain%20min_replies%3A1000%20min_faves%3A500%20min_retweets%3A200&src=typed_query&f=user");
            System.out.println("Start crawling data ...");

            Thread.sleep(3000);  // Initial wait for the page to load

            JavascriptExecutor js = (JavascriptExecutor) driver;
            int scrollCount = 0;
            int maxScrolls = 10;  // Define the number of times to scroll

            while (scrollCount < maxScrolls) {
                // Wait for UserCell elements to be present
                List<WebElement> userCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[@data-testid='UserCell']")));

                // Check if any user cells were found
                if (userCells.isEmpty()) {
                    System.out.println("No user cells found.");
                    break;  // Exit if no user cells are found
                }

                // Process each user cell
                for (int i = 0; i < userCells.size(); i++) {
                    try {
                        // Re-fetch the user cell to avoid stale element reference
                        WebElement userCell = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//button[@data-testid='UserCell'])[" + (i + 1) + "]")));

                        // Use a list of profile links inside user cells
                        List<WebElement> leafSpans = userCell.findElements(By.xpath(".//a[@role = 'link']"));

                        // Process each span element
                        for (WebElement leafSpan : leafSpans) {
                            // Ensure the link is clickable and handle intercepted clicks
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(leafSpan)).click();
                            } catch (ElementClickInterceptedException e) {
                                System.out.println("Click intercepted. Attempting JavaScript click.");
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", leafSpan);
                            }

                            // Wait for the profile page to load and find the follower count element
                            try {
                                WebElement userNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@data-testid, 'UserName')]//span/span")));
                                String username = userNameElement.getText();

                                WebElement followersCountElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, 'followers')]//span/span")));
                                int followersCount = convertFollowerCountToInt(followersCountElement.getText());


                                System.out.println("User: " + username);
                                System.out.println("Followers: " + followersCount);

                                // List of potential KOl

                                try {
                                    List<WebElement> retweets = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[@data-testid='socialContext']")));
                                    System.out.println("Number of retweets found: " + retweets.size());

                                    if (retweets.isEmpty()) {
                                        System.out.println("No repost.");
                                    } else {
                                        List<WebElement> elements = driver.findElements(By.xpath("//span[@data-testid='socialContext']/following::span[2]"));
                                        System.out.println("Number of potential KOLs found: " + elements.size()); // Debugging line

                                        if (elements.isEmpty()) {
                                            System.out.println("No potential KOLs found.");
                                        } else {
                                            for (WebElement el : elements) {
                                                System.out.println("Potential KOL: " + el.getText());
                                                WebElement dateElement = el.findElement(By.xpath("following::time[1]")); // Giả sử ngày đăng là phần tử time ngay sau tên
                                                System.out.println("Posted date: " + dateElement.getAttribute("datetime")); // Hoặc dùng getText() nếu cần


                                                System.out.println();
                                            }
                                        }
                                    }
                                } catch (NoSuchElementException e) {
                                    System.out.println("No repost.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }




                            } catch (NoSuchElementException e) {
                                System.out.println("Unable to find follower count.");
                            }

                            // Navigate back to the search results
                            driver.navigate().back();
                            Thread.sleep(5000);  // Wait for the search results page to load back
                        }

                    } catch (StaleElementReferenceException e) {
                        System.out.println("Stale element reference encountered. Skipping this element.");
                    } catch (NoSuchElementException e) {
                        System.out.println("Element not found. Skipping.");
                    }
                }

                // Scroll down the page to load more users
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000);  // Wait for new content to load

                scrollCount++;  // Increment scroll counter
            }
        } catch (Exception e) {
            System.out.println("Error processing search results: " + e.getMessage());
        }
    }

    private List<String> extractUserList(String listType) throws InterruptedException {
        String listXPath = listType.equals("followers") ? "//a[contains(@href, 'followers')]" : "//a[contains(@href, 'following')]";
        driver.findElement(By.xpath(listXPath)).click(); // Open the follower/following list

        List<String> userList = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int scrollCount = 0;
        int maxScrolls = 10;

        while (scrollCount < maxScrolls) {
            try {
                List<WebElement> userCells = wait.until(presenceOfAllElementsLocatedBy(By.xpath("//button[@data-testid='UserCell']")));
                for (WebElement userCell : userCells) {
                    String username = userCell.findElement(By.xpath(".//span[not(*)]")).getText();
                    int followerCount = convertFollowerCountToInt(userCell.findElement(By.xpath(".//a[contains(@href, 'followers')]//span/span")).getText());
                    if (followerCount > 100000) {
                        userList.add(username);
                    }
                }

                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000);
                scrollCount++;
            } catch (Exception e) {
                System.out.println("Error while extracting user list: " + e.getMessage());
                break;
            }
        }

        driver.navigate().back(); // Return to profile page
        Thread.sleep(3000);
        return userList;
    }

    public int convertFollowerCountToInt(String followerCountText) {
        double count;
        if (followerCountText.endsWith("K")) {
            count = Double.parseDouble(followerCountText.replace("K", "")) * 1000;
        } else if (followerCountText.endsWith("M")) {
            count = Double.parseDouble(followerCountText.replace("M", "")) * 1_000_000;
        } else if (followerCountText.endsWith("B")) {
            count = Double.parseDouble(followerCountText.replace("B", "")) * 1_000_000_000;
        } else {
            count = Double.parseDouble(followerCountText.replace(",", ""));
        }
        return (int) count;
    }
}

