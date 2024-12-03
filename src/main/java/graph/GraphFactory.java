package graph;

import model.*;

import java.io.IOException;
import java.util.List;
import storage.DataRepository;
import storage.StorageHandler;
import utils.ObjectType;

public class GraphFactory {
    public static final double followWeight = 1.0;
    public static final double postWeight = 1.0;
    public static final double repostWeight = 1.0;

    private final DataRepository dataRepository;
    private final NodeDataHandle nodeDataHandle;

    public GraphFactory() {
        this.dataRepository = new StorageHandler();
        this.nodeDataHandle = new GraphNodeStorage();
    }

    public void load(ObjectType type, String filePath) throws IOException {
        dataRepository.load(type, filePath);
    }

    public DataModel get(ObjectType type, String filePath, String uniqueKey) throws IOException {
        return dataRepository.get(type, filePath, uniqueKey);
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
        graphFactory.load(ObjectType.USER, "KOLs.json");

        // add user node
        for (GraphNode node : userNodeList) {
            graph.addNode(node);

            User user = (User) graphFactory.get(ObjectType.USER, "KOLs.json", node.getKol().getProfileLink());
            graphFactory.addNode(user, node);
        }

        // add following edge
        for (GraphNode node : userNodeList) {
            User userNode = node.getKol();
            List<String> followersList = userNode.getFollowersList();

            for(String followerLink : followersList) {
                User followerUser = (User) graphFactory.get(ObjectType.USER, "KOLs.json", followerLink);
                GraphNode followerNode = graphFactory.getNode(followerUser);
                graph.addEdge(followerNode, node, followWeight);
            }
        }

        // ADD NEEDED USER NODE

        // add tweet node
        for (GraphNode tweetNode : tweetNodeList) {
            Tweet tweet = tweetNode.getTweet();
            User userPost = (User) graphFactory.get(ObjectType.USER, "KOLs.json", tweet.getAuthorProfileLink());  //  lỗi chưa co link trong KOL.json
            GraphNode userPostNode = graphFactory.getNode(userPost);

            // add tweet node, user post node
            graph.addNode(tweetNode);
            graph.addNode(userPostNode);

            // add post edge
            graph.addEdge(tweetNode, userPostNode, postWeight);

            // add repost edge
            for(String userRepostLink : tweet.getRepostList()) {
                User userRepost = (User) graphFactory.get(ObjectType.USER, "KOLs.json", userRepostLink);  //  lỗi chưa co link trong KOL.json
                GraphNode userRepostNode = graphFactory.getNode(userRepost);

                graph.addNode(userRepostNode);
                graph.addEdge(userRepostNode, tweetNode, repostWeight);
            }
        }

        return graph;
    }
}
