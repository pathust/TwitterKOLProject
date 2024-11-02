package scraper.extractor;

import model.Tweet;

import java.io.IOException;
import java.util.List;

public interface TweetDataExtractor {
    void extractData(String tweetLink) throws IOException;
    List<Tweet> extractTweets( int maxListSize);

}
