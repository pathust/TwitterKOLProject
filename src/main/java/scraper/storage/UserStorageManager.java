package scraper.storage;

import model.User;

public class UserStorageManager extends StorageManager<User> {
    @Override
    protected Storage<User> createStorage() {
        return new UserStorage();
    }

    @Override
    protected String getUniqueKey(User user) {
        return user.getProfileLink();
    }
}
