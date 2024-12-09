package storage;

import model.DataModel;
import model.Tweet;
import model.User;
import utils.ObjectType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageHandler {
    private final Map<ObjectType, StorageManager<? extends DataModel>> storageMap;

    public StorageHandler() {
        storageMap = new HashMap<>();
        storageMap.put(ObjectType.USER, new StorageManager<User>() {});
        storageMap.put(ObjectType.TWEET, new StorageManager<Tweet>() {});
    }

    @SuppressWarnings("unchecked")
    private <T extends DataModel> StorageManager<T> getStorageManager(ObjectType type) {
        StorageManager<?> manager = storageMap.get(type);
        if (manager == null) {
            throw new IllegalArgumentException("Unsupported ObjectType: " + type);
        }
        return (StorageManager<T>) manager;
    }

    public void load(ObjectType type, String filePath) throws IOException {
        StorageManager<DataModel> manager = getStorageManager(type);
        manager.load(filePath, getModelClass(type));
    }

    public void add(ObjectType type, String filePath, DataModel item) throws IOException {
        System.out.println("saving " + item.getUniqueKey());
        try {
            StorageManager<DataModel> manager = getStorageManager(type);
            manager.add(filePath, item, getModelClass(type));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(ObjectType type, String filePath) throws IOException {
        StorageManager<DataModel> manager = getStorageManager(type);
        manager.save(filePath, getModelClass(type));
    }

    public List<? extends DataModel> getAll(ObjectType type, String filePath) throws IOException {
        StorageManager<DataModel> manager = getStorageManager(type);
        return manager.getAll(filePath, getModelClass(type));
    }

    public void transferToMainStorage(ObjectType type, String filePath, DataModel item) throws IOException {
        StorageManager<DataModel> manager = getStorageManager(type);
        manager.transferToMainStorage(item, filePath, getModelClass(type));
    }

    public boolean exists(ObjectType type, String filePath, String uniqueKey) throws IOException {
        StorageManager<DataModel> manager = getStorageManager(type);
        return manager.exists(filePath, uniqueKey, getModelClass(type));
    }

    public DataModel get(ObjectType type, String filePath, String uniqueKey) throws IOException {
        StorageManager<DataModel> manager = getStorageManager(type);
        return manager.get(filePath, uniqueKey, getModelClass(type));
    }

    private <T extends DataModel> Class<T> getModelClass(ObjectType type) {
        switch (type) {
            case USER:
                return (Class<T>) User.class;
            case TWEET:
                return (Class<T>) Tweet.class;
            default:
                throw new IllegalArgumentException("Unsupported ObjectType: " + type);
        }
    }
}
