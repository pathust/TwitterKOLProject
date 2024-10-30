package scraper.extractor;

import java.io.IOException;
import java.util.List;

public interface TweetDataExtractor {
    void extractData(String tweetLink) throws InterruptedException, IOException;
    void extractTweetTo(String filePath, boolean isVerified) throws InterruptedException, IOException;
}