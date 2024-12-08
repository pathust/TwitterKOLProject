package storage;

import model.DataModel;
import model.Tweet;
import model.User;
import storage.main.TweetMainStorage;
import storage.main.UserMainStorage;
import storage.temporary.TemporaryStorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StorageManager<T extends DataModel> {
    private final Map<String, Storage<T>> storageMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    private Storage<T> createStorage(Class<T> clazz) {
        if (clazz == User.class) {
            return (Storage<T>) new Storage<>(new UserMainStorage(), new TemporaryStorage());
        } else if (clazz == Tweet.class) {
            return (Storage<T>) new Storage<>(new TweetMainStorage(), new TemporaryStorage());
        } else {
            throw new IllegalArgumentException("Unsupported class: " + clazz.getName());
        }
    }

    private Storage<T> getStorage(String filePath, Class<T> clazz) {
        return storageMap.computeIfAbsent(filePath, key -> createStorage(clazz));
    }

    public void load(String filePath, Class<T> clazz) throws IOException {
        Storage<T> storage = getStorage(filePath, clazz);
        storage.load(filePath);
    }

    public void add(String filePath, T item, Class<T> clazz) throws IOException {
        Storage<T> storage = getStorage(filePath, clazz);
        storage.add(item);
    }

    public void transferToMainStorage(T item, String filePath, Class<T> clazz) throws IOException {
        Storage<T> storage = getStorage(filePath, clazz);
        storage.transferToMainStorage(item);
    }

    public void save(String filePath, Class<T> clazz) throws IOException {
        Storage<T> storage = getStorage(filePath, clazz);
        storage.save(filePath);
    }

    public List<T> getAll(String filePath, Class<T> clazz) {
        Storage<T> storage = getStorage(filePath, clazz);
        return storage.getAll();
    }

    public boolean exists(String filePath, String uniqueKey, Class<T> clazz) {
        Storage<T> storage = getStorage(filePath, clazz);
        return storage.exists(uniqueKey);
    }

    public T get(String filePath, String uniqueKey, Class<T> clazz) throws IOException {
        Storage<T> storage = getStorage(filePath, clazz);
        return storage.get(uniqueKey);
    }
}
