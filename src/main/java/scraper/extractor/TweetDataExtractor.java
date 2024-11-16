package scraper.extractor;

import model.Tweet;
import model.User;
import java.io.IOException;
import java.util.List;

public interface TweetDataExtractor {
    void extractData(String tweetLink, int repostCountThreshold, int maxNewUser) throws IOException;
    List<Tweet> extractTweets( int maxListSize, int maxNewUser);
    List<Tweet> extractEachTweet( int maxListSize, int maxNewUser);
}
