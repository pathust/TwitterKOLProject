package storage.main;

public class TweetStorageManager extends StorageManager {
    @Override
    protected Storage createStorage() {
        return new TweetStorage();
    }
}
