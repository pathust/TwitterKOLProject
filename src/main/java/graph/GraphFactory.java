package graph;

import model.*;

import java.io.IOException;
import java.util.List;


public class GraphFactory {
    public static final double followWeight = 1.0;
    public static final double postWeight = 1.0;
    public static final double repostWeight = 1.0;

    private final GraphNodeStorage graphNodeStorage;

    public GraphFactory() {
        this.graphNodeStorage = new GraphNodeStorage();
    }

    public void addNode(String uniqueKey, GraphNode node) {
        graphNodeStorage.addNode(uniqueKey, node);
    }

    public GraphNode getNode(String uniqueKey) {
        return graphNodeStorage.getNode(uniqueKey);
    }

    public static Graph createGraph(List<GraphNode> userNodeList, List<GraphNode> tweetNodeList) throws IOException {
        GraphFactory graphFactory = new GraphFactory();
        Graph graph = new Graph();

        // add user node
        for (GraphNode node : userNodeList) {
            graph.addNode(node);

            String userLink = node.getDataModel().getUniqueKey();
            graphFactory.addNode(userLink, node);
        }

        // add following edge
        for (GraphNode node : userNodeList) {
            User userNode = (User) node.getDataModel();
            List<String> followersList = userNode.getFollowersList();

            for(String followerLink : followersList) {
                GraphNode followerNode = graphFactory.getNode(followerLink);
                graph.addEdge(followerNode, node, followWeight);
            }
        }

        // add tweet node
        for (GraphNode tweetNode : tweetNodeList) {
            Tweet tweet = (Tweet) tweetNode.getDataModel();
            String authorProfileLink = tweet.getAuthorProfileLink();  //  lỗi chưa co link trong KOL.json
            GraphNode authorNode = graphFactory.getNode(authorProfileLink);

            // add tweet node, user post node
            graph.addNode(tweetNode);
            graph.addNode(authorNode);

            // add post edge
            graph.addEdge(tweetNode, authorNode, postWeight);

            // add repost edge
            for(String userRepostLink : tweet.getRepostList()) {
                GraphNode userRepostNode = graphFactory.getNode(userRepostLink);

                graph.addNode(userRepostNode);
                graph.addEdge(userRepostNode, tweetNode, repostWeight);
            }
        }

        return graph;
    }
}
