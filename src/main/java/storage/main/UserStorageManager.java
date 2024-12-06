package storage.main;

public class UserStorageManager extends StorageManager {
    @Override
    protected Storage createStorage() {
        return new UserStorage();
    }
}
