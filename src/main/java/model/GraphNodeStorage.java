package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GraphNodeStorage implements NodeDataHandle {
    private Map<User, GraphNode> graphUserNodeMap;
    private Map<Tweet, GraphNode> graphTweetNodeMap;


    public GraphNodeStorage() {
        graphUserNodeMap = new HashMap<>();
        graphTweetNodeMap = new HashMap<>();
    }

    public void addNode(User user, GraphNode userNode) {
        graphUserNodeMap.put(user, userNode);
    }

    public void addNode(Tweet tweet, GraphNode tweetNode) {
        graphTweetNodeMap.put(tweet, tweetNode);
    }

    public GraphNode getNode(User user) {
        return graphUserNodeMap.get(user);
    }

    public GraphNode getNode(Tweet tweet) {
        return graphTweetNodeMap.get(tweet);
    }
}
