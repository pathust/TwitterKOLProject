package model;

import java.io.IOException;

public interface NodeDataHandle {
    void addNode(User user, GraphNode userNode) throws IOException;
    GraphNode getNode(User user) throws IOException;
    void addNode(Tweet tweet, GraphNode tweetNode) throws IOException;
    GraphNode getNode(Tweet tweet) throws IOException;
}
