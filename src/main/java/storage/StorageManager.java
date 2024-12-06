package storage;

import model.DataModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StorageManager{
    private final Map<String, Storage> storageMap = new HashMap<>();

    protected abstract Storage createStorage();

    private Storage getStorage(String filePath) {
        return storageMap.computeIfAbsent(filePath, key -> createStorage());
    }

    public void load(String filePath) throws IOException {
        Storage storage = getStorage(filePath);
        storage.load(filePath);
    }

    public void add(String filePath, DataModel item) throws IOException {
        Storage storage = getStorage(filePath);
        storage.add(item);
    }

    public void save(String filePath) throws IOException {
        Storage storage = getStorage(filePath);
        storage.save(filePath);
    }

    public List getAll(String filePath) throws IOException {
        Storage storage = getStorage(filePath);
        return storage.getAll();
    }

    public boolean exists(String filePath, String uniqueKey) throws IOException {
        Storage storage = getStorage(filePath);
        return storage.exists(uniqueKey);
    }

    public DataModel get(String filePath, String uniqueKey) throws IOException {
        Storage storage = getStorage(filePath);
        return storage.get(uniqueKey);
    }

    protected abstract String getUniqueKey(DataModel item);
}