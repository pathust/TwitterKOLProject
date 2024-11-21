package graph;

import model.GraphNode;
import model.Tweet;
import scraper.storage.TweetDataHandler;
import scraper.storage.TweetStorageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TweetNodeExtract {
    public static List<GraphNode> extract() throws IOException {
        TweetDataHandler tweetDataHandler = new TweetStorageManager();
        tweetDataHandler.loadTweets("Tweet.json");
        List<Tweet> tweetList = tweetDataHandler.getTweets("Tweet.json");

        List<GraphNode> nodeList = new ArrayList<>();

        for (Tweet tweet : tweetList) {
           nodeList.add(new GraphNode(tweet));
        }

        return nodeList;
    }
}
