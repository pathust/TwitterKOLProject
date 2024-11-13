package scraper.storage;

import model.Tweet;
import model.User;

import java.io.IOException;
import java.util.List;

public interface TweetDataHandler {
    void loadTweets(String filePath) throws IOException;
    void addTweet(String filePath, Tweet newTweet) throws IOException;
    void saveData(String filePath) throws IOException;
    List <Tweet> getTweets (String filepath) throws IOException;
    boolean userExists(String filePath, String profileLink) throws IOException;
    Tweet getTweet(String filePath, String profileLink) throws IOException;
}




