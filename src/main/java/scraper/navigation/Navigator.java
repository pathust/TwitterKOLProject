package scraper.navigation;

import org.openqa.selenium.WebElement;

public interface Navigator {
    void scrollToElement(WebElement element);

    void scrollDown();

    void clickButton(WebElement element, String buttonName);

    void fillingFieldBySpan(String spanText, String text);

    void navigateToSection(String section);

    void scrollBy(int pixels);

    String getLink(WebElement element);
}