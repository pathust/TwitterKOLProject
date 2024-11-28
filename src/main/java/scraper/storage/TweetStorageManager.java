package scraper.storage;

import model.Tweet;

public class TweetStorageManager extends StorageManager<Tweet> {
    @Override
    protected Storage<Tweet> createStorage() {
        return new TweetStorage();
    }

    @Override
    protected String getUniqueKey(Tweet tweet) {
        return tweet.getTweetLink();
    }
}
