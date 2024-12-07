package storage;

import model.DataModel;
import storage.main.StorageManager;
import storage.main.TweetStorageManager;
import storage.main.UserStorageManager;
import utils.ObjectType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageHandler {
    private final Map<ObjectType, StorageManager> map;

    public StorageHandler() {
        map = new HashMap<>();
        map.put(ObjectType.USER, new UserStorageManager());
        map.put(ObjectType.TWEET, new TweetStorageManager());
    }

    public void load(ObjectType type, String filePath) throws IOException {
        StorageManager storageManager = map.get(type);
        storageManager.load(filePath);
    }

    public void add(ObjectType type, String filePath, DataModel item) throws IOException {
        StorageManager storageManager = map.get(type);
        storageManager.add(filePath, item);
    }

    public void save(ObjectType type, String filePath) throws IOException {
        StorageManager storageManager = map.get(type);
        storageManager.save(filePath);
    }

    public List<DataModel> getAll(ObjectType type, String filePath) throws IOException {
        StorageManager storageManager = map.get(type);
        return storageManager.getAll(filePath);
    }

    public boolean exists(ObjectType type, String filePath, String uniqueKey) throws IOException {
        StorageManager storageManager = map.get(type);
        return storageManager.exists(filePath, uniqueKey);
    }

    public DataModel get(ObjectType type, String filePath, String uniqueKey) throws IOException {
        StorageManager storageManager = map.get(type);
        return storageManager.get(filePath, uniqueKey);
    }
}
