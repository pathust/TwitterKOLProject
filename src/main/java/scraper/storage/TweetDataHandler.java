package scraper.storage;

import model.Tweet;

import java.io.IOException;
import java.util.List;

public interface TweetDataHandler {
    void addTweet(String filePath, Tweet newTweet) throws IOException;
    void saveData(String filePath) throws IOException;
    List<String> getTweetContentsFrom(String filePath) throws IOException;
    List <Tweet> getTweets (String filepath) throws IOException;
}




