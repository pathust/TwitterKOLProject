package scraper.navigation;

import org.openqa.selenium.WebElement;

public interface Navigator {
    void scrollToElement(WebElement element);

    void scrollDown();

    void clickButton(String parentXPath, String buttonName);

    void fillingFieldBySpan(String spanText, String text);

    void navigateToSection(String section);

    void wait(int milliseconds);

    void scrollBy(int pixels);

    String getLink(WebElement element);
}