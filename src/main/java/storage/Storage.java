package storage;

import model.DataModel;
import storage.main.MainStorage;
import storage.temporary.TemporaryStorage;

import java.io.IOException;
import java.util.List;

public class Storage<T extends DataModel> {
    private final MainStorage<T> mainStorage;
    private final TemporaryStorage temporaryStorage;

    public Storage(MainStorage<T> mainStorage, TemporaryStorage temporaryStorage) {
        this.mainStorage = mainStorage;
        this.temporaryStorage = temporaryStorage;
    }

    public TemporaryStorage getTemporaryStorage() {
        return temporaryStorage;
    }

    public MainStorage<T> getMainStorage() {
        return mainStorage;
    }

    public void load(String filePath) throws IOException {
        mainStorage.load(filePath + ".json");
        temporaryStorage.load(filePath + ".txt");
    }

    public void save(String filePath) throws IOException {
        mainStorage.save(filePath + ".json");
        temporaryStorage.save(filePath + ".txt");
    }

    public void add(T item) {
        mainStorage.add(item);
        temporaryStorage.add(item.getUniqueKey());
    }

    public void transferToMainStorage(T item) {
        mainStorage.add(item);
        temporaryStorage.setProcessed();
    }

    public boolean exists(String identifier) {
        return mainStorage.exists(identifier);
    }

    public T get(String identifier) {
        return mainStorage.get(identifier);
    }

    public List<T> getAll() {
        return mainStorage.getAll();
    }

    public List<String> getUnprocessedItemUniqueKeys() {
        return temporaryStorage.getUnprocessedItemUniqueKeys();
    }
}
