package scraper;

public class TwitterScraperController {
    public static void main(String[] args) {
        TwitterScraper crawler = new TwitterScraper();

        // Provide your Twitter username and password directly
        String username = "@21Oop36301";
        String password = "123456789@21oop";

        crawler.login(username, password);

    }
}
