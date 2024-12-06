package scraper.extractor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import scraper.navigation.Navigator;
import storage.main.StorageHandler;

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

    public DataExtractor(WebDriver driver, Navigator navigator, StorageHandler storageHandler) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.navigator = navigator;
        this.storageHandler = storageHandler;
    }

    protected abstract WebElement getFirstCell();
    protected abstract WebElement nextCell(WebElement currentCell);
    protected abstract T extractItem(String xpathExpression, boolean addToStorage);
    protected abstract void Write(T item);
    public abstract void extractData(String link) throws IOException;
    public List<T> extractItems(int maxListSize, boolean addToStorage) {
        List<T> items = new ArrayList<>();
        if (maxListSize == 0) {
            return items;
        }

        WebElement currentCell = null;
        int counter = 1;
        do {
            currentCell = nextCell(currentCell);
            if (currentCell == null) {
                break;
            }
            navigator.scrollToElement(currentCell);
            String xpathExpression = getXPath(driver, currentCell);
            System.out.println(xpathExpression);
            T newItem = extractItem(xpathExpression, addToStorage);
            System.out.println(counter);
            Write(newItem);
            items.add(newItem);
        } while (++counter <= maxListSize);

        return items;
    }
}
