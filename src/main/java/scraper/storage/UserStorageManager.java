package scraper.storage;

import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserStorageManager implements DataHandler {
    private final Map<String, TwitterUserStorage> storageMap;

    public UserStorageManager() {
        this.storageMap = new HashMap<>();
    }

    private TwitterUserStorage getStorage(String filePath) throws IOException {
        TwitterUserStorage storage = storageMap.get(filePath);
        if (storage == null) {
            storage = new TwitterUserStorage(filePath);
            storageMap.put(filePath, storage);
        }
        return storage;
    }

    @Override
    public void addUser(String filePath, User user) throws IOException {
        TwitterUserStorage storage = getStorage(filePath);
        storage.addUser(user);
    }

    @Override
    public void saveData(String filePath) throws IOException {
        TwitterUserStorage storage = getStorage(filePath);
        storage.saveData(filePath);
    }

    @Override
    public List<String> getUserLinksFrom(String filePath) throws IOException {
        TwitterUserStorage storage = getStorage(filePath);
        return storage.getUserLinksFrom(filePath);
    }


}
