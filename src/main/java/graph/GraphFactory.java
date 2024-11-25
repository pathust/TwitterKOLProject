package graph;

import model.GraphNode;
import model.Tweet;
import model.User;

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

    public GraphFactory() {
        this.userDataHandler = new UserStorageManager();
    }

    public void loadUsers(String filePath) throws IOException {
        userDataHandler.loadUsers(filePath);
    }

    public User getUser(String filePath, String profileLink) throws IOException {
        return userDataHandler.getUser(filePath, profileLink);
    }

    public static Graph createGraph(List<GraphNode> userNodeList, List<GraphNode> tweetNodeList) throws IOException {
        GraphFactory graphFactory = new GraphFactory();
        Graph graph = new Graph();
        graphFactory.loadUsers("KOLs.json");

        // add user node
        for (GraphNode node : userNodeList) {
            graph.addNode(node);
        }

        // add following edge
        for (GraphNode node : userNodeList) {
            User userNode = node.getKol();
            List<String> followingList = userNode.getFollowersList();

            for(String userLink : followingList) {
                GraphNode targetNode = new GraphNode(graphFactory.getUser("KOLs.json", userLink));
                graph.addEdge(node, targetNode, followWeight);

            }
        }

        // test add random edge

//        Random rand = new Random();
//        for (GraphNode node : userNodeList) {
//            for (GraphNode otherNode : userNodeList) {
//                if(node != otherNode) {
//                    if(rand.nextBoolean()) {
//                        graph.addEdge(node, otherNode, 1.0);
////                        System.out.println(node.getKol().getUsername() + " " + otherNode.getKol().getUsername());
//                    }
//                }
//            }
//        }

        for (GraphNode tweetNode : tweetNodeList) {
            Tweet tweet = tweetNode.getTweet();
            String userPostLink = tweet.getUserLink();
            GraphNode userPostNode = new GraphNode(graphFactory.getUser("KOLs.json", userPostLink));

            // add tweet node, user post node
            graph.addNode(tweetNode);
            graph.addNode(userPostNode);

            // add post edge
            graph.addEdge(userPostNode, tweetNode, postWeight);

            // add repost edge
            for(String userRepostLink : tweet.getRepostList()) {
                GraphNode userRepostNode = new GraphNode(graphFactory.getUser("KOLs.json", userRepostLink));

                graph.addNode(userRepostNode);
                graph.addEdge(tweetNode, userRepostNode, repostWeight);
            }
        }

        return graph;
    }
}
