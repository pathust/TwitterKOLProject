package graph;

import model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import scraper.storage.UserDataHandler;
import scraper.storage.UserStorageManager;

public class GraphFactory {
    public static final double followWeight = 1.0;
    public static final double postWeight = 1.0;
    public static final double repostWeight = 1.0;

    private final UserDataHandler userDataHandler;
    private final NodeDataHandle nodeDataHandle;

    public GraphFactory() {
        this.userDataHandler = new UserStorageManager();
        this.nodeDataHandle = new GraphNodeStorage();
    }

    public void loadUsers(String filePath) throws IOException {
        userDataHandler.loadUsers(filePath);
    }

    public User getUser(String filePath, String profileLink) throws IOException {
        return userDataHandler.getUser(filePath, profileLink);
    }

    public void addNode(User user, GraphNode userNode) throws IOException {
        nodeDataHandle.addNode(user, userNode);
    }

    public void addNode(Tweet tweet, GraphNode tweetNode) throws IOException {
        nodeDataHandle.addNode(tweet, tweetNode);
    }

    public GraphNode getNode(User user) throws IOException {
        return nodeDataHandle.getNode(user);
    }

    public GraphNode getNode(Tweet tweet) throws IOException {
        return nodeDataHandle.getNode(tweet);
    }

    public static Graph createGraph(List<GraphNode> userNodeList, List<GraphNode> tweetNodeList) throws IOException {
        GraphFactory graphFactory = new GraphFactory();
        Graph graph = new Graph();
        graphFactory.loadUsers("KOLs.json");

        // add user node
        for (GraphNode node : userNodeList) {
            graph.addNode(node);

            User user = graphFactory.getUser("KOLs.json", node.getKol().getProfileLink());
            graphFactory.addNode(user, node);
        }

        // add following edge
        for (GraphNode node : userNodeList) {
            User userNode = node.getKol();
            List<String> followingList = userNode.getFollowersList();

            for(String targetUserLink : followingList) {
                User targetUser = graphFactory.getUser("KOLs.json", targetUserLink);
                GraphNode targetNode = graphFactory.getNode(targetUser);
                graph.addEdge(node, targetNode, followWeight);
            }
        }

        // add tweet node
        for (GraphNode tweetNode : tweetNodeList) {
            Tweet tweet = tweetNode.getTweet();
            User userPost = graphFactory.getUser("KOLs.json", tweet.getUserLink());
            GraphNode userPostNode = graphFactory.getNode(userPost);

            // add tweet node, user post node
            graph.addNode(tweetNode);
            graph.addNode(userPostNode);

            // add post edge
            graph.addEdge(userPostNode, tweetNode, postWeight);

            // add repost edge
            for(String userRepostLink : tweet.getRepostList()) {
                User userRepost = graphFactory.getUser("KOLs.json", userRepostLink);
                GraphNode userRepostNode = graphFactory.getNode(userRepost);

                graph.addNode(userRepostNode);
                graph.addEdge(tweetNode, userRepostNode, repostWeight);
            }
        }

        return graph;
    }
}
