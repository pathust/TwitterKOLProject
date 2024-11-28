package scraper.storage;

import java.io.IOException;
import java.util.List;

public interface StorageHandler<T> {
    void load(String filePath) throws IOException;
    void add(String filePath, T item) throws IOException;
    void save(String filePath) throws IOException;
    List<T> getAll(String filePath) throws IOException;
    boolean exists(String filePath, String uniqueKey) throws IOException;
    T get(String filePath, String uniqueKey) throws IOException;
}
