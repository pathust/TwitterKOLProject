package scraper.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StorageManager<T> implements StorageHandler<T> {
    private final Map<String, Storage<T>> storageMap = new HashMap<>();

    protected abstract Storage<T> createStorage();

    private Storage<T> getStorage(String filePath) {
        return storageMap.computeIfAbsent(filePath, key -> createStorage());
    }

    @Override
    public void load(String filePath) throws IOException {
        Storage<T> storage = getStorage(filePath);
        storage.load(filePath);
    }

    @Override
    public void add(String filePath, T item) throws IOException {
        Storage<T> storage = getStorage(filePath);
        storage.add(item);
    }

    @Override
    public void save(String filePath) throws IOException {
        Storage<T> storage = getStorage(filePath);
        storage.save(filePath);
    }

    @Override
    public List<T> getAll(String filePath) throws IOException {
        Storage<T> storage = getStorage(filePath);
        return storage.getAll();
    }

    @Override
    public boolean exists(String filePath, String uniqueKey) throws IOException {
        Storage<T> storage = getStorage(filePath);
        return storage.exists(uniqueKey);
    }

    @Override
    public T get(String filePath, String uniqueKey) throws IOException {
        Storage<T> storage = getStorage(filePath);
        return storage.get(uniqueKey);
    }

    protected abstract String getUniqueKey(T item);
}