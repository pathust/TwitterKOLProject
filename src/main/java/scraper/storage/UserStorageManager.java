package scraper.storage;

import model.DataModel;
import model.User;

public class UserStorageManager extends StorageManager {
    @Override
    protected Storage createStorage() {
        return new UserStorage();
    }

    @Override
    protected String getUniqueKey(DataModel dataModel) {
        User user = (User) dataModel;
        return user.getProfileLink();
    }
}
