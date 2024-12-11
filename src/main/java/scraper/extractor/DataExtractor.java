package scraper.extractor;

import model.DataModel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import storage.StorageHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static utils.XPathExtension.getXPath;

public abstract class DataExtractor<T extends DataModel> {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Navigator navigator;
    protected final StorageHandler storageHandler;

    public DataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
        this.storageHandler = storageHandler;
    }

    protected abstract WebElement getFirstCell();
    protected abstract WebElement nextCell(WebElement currentCell) throws RuntimeException;
    protected abstract T extractItem(String filePath, String xpathExpression, boolean addToStorage) throws IOException;
    public abstract void extractData(String filePath, String key) throws IOException, InterruptedException;

    public List<T> extractItems(String filePath, int maxListSize, boolean addToStorage) throws InterruptedException, IOException {
        List<T> items = new ArrayList<>();
        if (maxListSize == 0) {
            return items;
        }

        WebElement previousCell = null, currentCell;
        int counter = 1;
        do {
            while (true) {
                currentCell = nextCell(previousCell);
                if (currentCell != null || driver.findElements(By.xpath("//button//span[text() = 'Try Again']")).isEmpty()) {
                    break;
                }
                String currentURL = driver.getCurrentUrl();
                System.out.println(currentURL);
                navigator.wait(180_000);
                driver.get(currentURL);
            }
            if (currentCell == null) {
                break;
            }

            navigator.scrollToElement(currentCell);
            String xpathExpression = getXPath(driver, currentCell);
            T newItem = extractItem(filePath, xpathExpression, addToStorage);

            if (newItem != null)
                items.add(newItem);

            previousCell = currentCell;
        } while (++counter <= maxListSize);
        System.out.println("Retrieved " + items.size() + " items");
        return items;
    }
}
