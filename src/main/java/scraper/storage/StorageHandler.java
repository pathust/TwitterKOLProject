package scraper.storage;

import model.DataModel;
import model.Tweet;
import model.User;
import utils.ObjectType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageHandler implements DataRepository{
    private UserStorageManager userStorageManager;
    private TweetStorageManager tweetStorageManager;
    private Map<ObjectType, StorageManager> map;

    public StorageHandler() {
        this.userStorageManager = new UserStorageManager();
        this.tweetStorageManager = new TweetStorageManager();
        map = new HashMap<>();
        map.put(ObjectType.USER, userStorageManager);
        map.put(ObjectType.TWEET, tweetStorageManager);
    }

    @Override
    public void load(ObjectType type, String filePath) throws IOException {
        StorageManager storageManager = map.get(type);
        storageManager.load(filePath);
    }

    @Override
    public void add(ObjectType type, String filePath, DataModel item) throws IOException {
        StorageManager storageManager = map.get(type);
        storageManager.add(filePath, item);
    }

    @Override
    public void save(ObjectType type, String filePath) throws IOException {
        StorageManager storageManager = map.get(type);
        storageManager.save(filePath);
    }

    @Override
    public List<DataModel> getAll(ObjectType type, String filePath) throws IOException {
        StorageManager storageManager = map.get(type);
        return storageManager.getAll(filePath);
    }

    @Override
    public boolean exists(ObjectType type, String filePath, String uniqueKey) throws IOException {
        StorageManager storageManager = map.get(type);
        return storageManager.exists(filePath, uniqueKey);
    }

    @Override
    public DataModel get(ObjectType type, String filePath, String uniqueKey) throws IOException {
        StorageManager storageManager = map.get(type);
        return storageManager.get(filePath, uniqueKey);
    }
}
