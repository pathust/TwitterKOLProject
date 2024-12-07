package graph;

import model.*;
import utils.ObjectType;

import java.io.IOException;
import java.util.List;

public class GraphFactory {
    public static final double followWeight = 1.0;
    public static final double postWeight = 1.0;
    public static final double repostWeight = 1.0;

    private static final GraphNodeStorage graphNodeStorage = new GraphNodeStorage();

    private static void add(ObjectType type, Graph graph, List<GraphNode> nodeList, double inWeight) {
        add(type, graph, nodeList, inWeight, 0.0); // Call the overloaded method with default outWeight
    }

    private static void add(ObjectType type, Graph graph, List<GraphNode> nodeList, double inWeight, double outWeight) {
        for (GraphNode node : nodeList) {
            graph.addNode(node);
            node.setType(type);
            graphNodeStorage.addNode(node.getDataModel().getUniqueKey(), node);
        }

        for (GraphNode node : nodeList) {
            List<String> interactors = getInteractors(type, node);
            for (String uniqueKey : interactors) {
                graph.addEdge(graphNodeStorage.getNode(uniqueKey), node, inWeight);
            }

            String uniqueKey = getParent(type, node);
            if(uniqueKey != null) {
                graph.addEdge(node, graphNodeStorage.getNode(uniqueKey), outWeight);
            }
        }
    }

    private static List<String> getInteractors(ObjectType type, GraphNode node) {
        if(type == ObjectType.USER) {
            return ((User) node.getDataModel()).getFollowersList();
        }

        if(type == ObjectType.TWEET) {
            return ((Tweet) node.getDataModel()).getRepostList();
        }

        return null;
    }

    private static String getParent(ObjectType type, GraphNode node) {
        if(type == ObjectType.TWEET) {
            return ((Tweet) node.getDataModel()).getAuthorProfileLink();
        }

        return null;
    }

    public static Graph createGraph(List<GraphNode> userNodeList, List<GraphNode> tweetNodeList) throws IOException {
        Graph graph = new Graph();

        // add node
        add(ObjectType.USER, graph, userNodeList, followWeight);
        add(ObjectType.TWEET, graph, tweetNodeList, repostWeight, postWeight);

        return graph;
    }
}
