package scraper.extractor;

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

public abstract class DataExtractor<T> {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Navigator navigator;
    protected final StorageHandler storageHandler;

    private final int MAX_RETRY_COUNT = 3;

    public DataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
        this.storageHandler = storageHandler;
    }

    protected abstract WebElement getFirstCell();
    protected abstract WebElement nextCell(WebElement currentCell) throws InterruptedException;
    protected abstract T extractItem(String xpathExpression, boolean addToStorage);
    public abstract void extractData(String link) throws IOException, InterruptedException;
    public List<T> extractItems(int maxListSize, boolean addToStorage) throws InterruptedException {
        List<T> items = new ArrayList<>();
        if (maxListSize == 0) {
            return items;
        }

        WebElement previousCell = null, currentCell = null;
        int counter = 1;
        do {
            currentCell = nextCell(previousCell);
            if (currentCell == null) {
                break;
            }

            System.out.println("Retrieving " + counter);

            navigator.scrollToElement(currentCell);
            System.out.println("Scrolled to " + counter);
            String xpathExpression = getXPath(driver, currentCell);
            T newItem = extractItem(xpathExpression, addToStorage);
            System.out.println("Retrieved " + counter);
            items.add(newItem);

            previousCell = currentCell;
        } while (++counter <= maxListSize);
        System.out.println("Retrieved " + items.size() + " items");
        return items;
    }
}
