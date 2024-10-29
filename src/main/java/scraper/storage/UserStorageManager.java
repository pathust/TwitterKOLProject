package scraper.storage;

import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserStorageManager implements UserDataHandler {
    private final Map<String, UserStorage> storageMap;

    public UserStorageManager() {
        this.storageMap = new HashMap<>();
    }

    private UserStorage getStorage(String filePath) throws IOException {
        UserStorage storage = storageMap.get(filePath);
        if (storage == null) {
            storage = new UserStorage(filePath, true);
            storageMap.put(filePath, storage);
        }
        return storage;
    }

    @Override
    public void addUser(String filePath, User user) throws IOException {
        UserStorage storage = getStorage(filePath);
        storage.addUser(user);
    }

    @Override
    public void saveData(String filePath) throws IOException {
        UserStorage storage = getStorage(filePath);
        storage.saveData(filePath);
    }

    @Override
    public List<String> getUserLinksFrom(String filePath) throws IOException {
        UserStorage storage = getStorage(filePath);
        return storage.getUserLinks();
    }


}
