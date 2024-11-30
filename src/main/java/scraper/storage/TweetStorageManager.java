package scraper.storage;

import model.DataModel;
import model.Tweet;

public class TweetStorageManager extends StorageManager {
    @Override
    protected Storage createStorage() {
        return new TweetStorage();
    }

    @Override
    protected String getUniqueKey(DataModel dataModel) {
        Tweet tweet = (Tweet) dataModel;
        return tweet.getTweetLink();
    }
}
