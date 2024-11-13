package scraper.storage;

import model.Tweet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetStorageManager implements TweetDataHandler {
    private final Map<String, TweetStorage> storageMap;

    public TweetStorageManager() {
        this.storageMap = new HashMap<>();
    }

    private TweetStorage getStorage(String filePath) throws IOException {
        TweetStorage storage = storageMap.get(filePath);
        if (storage == null) {
            storage = new TweetStorage();
            storageMap.put(filePath, storage);
        }
        return storage;
    }

    @Override
    public void loadTweets(String filePath) throws IOException {
        TweetStorage storage = getStorage(filePath);
        storage.loadTweets(filePath);
    }

    @Override
    public void addTweet(String filePath, Tweet tweet) throws IOException {
        TweetStorage storage = getStorage(filePath);
        storage.addTweet(tweet);
    }

    @Override
    public void saveData(String filePath) throws IOException {
        TweetStorage storage = getStorage(filePath);
        storage.saveData(filePath);
    }

    @Override
    public List<Tweet> getTweets(String filePath) throws IOException {
        TweetStorage storage = getStorage(filePath);
        return storage.getTweets();
    }

    @Override
    public boolean userExists(String filePath, String profileLink) throws IOException {
        TweetStorage storage = getStorage(filePath);
        return storage.tweetExists(profileLink);
    }

    @Override
    public Tweet getTweet(String filePath, String profileLink) throws IOException {
        TweetStorage storage = getStorage(filePath);
        return storage.getTweet(profileLink);
    }
}
