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
        this.mainStorage.load(filePath + ".json");
        this.temporaryStorage.load(filePath + ".txt");
    }

    public void save(String filePath) throws IOException {
        System.out.println(filePath);
        this.mainStorage.save(filePath + ".json");
        this.temporaryStorage.save(filePath + ".txt");
    }

    public void add(T item) {
        System.out.println("add " + item.getUniqueKey());
        this.mainStorage.add(item);
        this.temporaryStorage.add(item.getUniqueKey());
        System.out.println(this.temporaryStorage.getTemporaryState().getItemUniqueKeys().size());
    }

    public void transferToMainStorage(T item) {
        this.mainStorage.add(item);
        this.temporaryStorage.setProcessed();
    }

    public boolean exists(String identifier) {
        return this.mainStorage.exists(identifier);
    }

    public T get(String identifier) {
        return this.mainStorage.get(identifier);
    }

    public List<T> getAll() {
        return this.mainStorage.getAll();
    }

    public List<String> getUnprocessedItemUniqueKeys() {
        return this.temporaryStorage.getUnprocessedItemUniqueKeys();
    }
}
