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

    private static void add(ObjectType type, Graph graph, List<DataModel> nodeList, double inWeight, double outWeight) {
        for (DataModel node : nodeList) {
            graph.addNode(node);
            graphNodeStorage.addNode(node.getUniqueKey(), node);
        }

        for (DataModel node : nodeList) {
            List<String> interactors = getInteractors(type, node);
            for (String uniqueKey : interactors) {
                if(!graphNodeStorage.containsNode(uniqueKey)) {
                    graphNodeStorage.addNode(uniqueKey, new User());
                }

                graph.addEdge(graphNodeStorage.getNode(uniqueKey), node, inWeight);
            }

            String uniqueKey = getParent(type, node);
            if(uniqueKey != null) {
                if(!graphNodeStorage.containsNode(uniqueKey)) {
                    graphNodeStorage.addNode(uniqueKey, new User());
                }

                graph.addEdge(node, graphNodeStorage.getNode(uniqueKey), outWeight);
            }
        }
    }

    private static List<String> getInteractors(ObjectType type, DataModel node) {
        if(type == ObjectType.USER) {
            return ((User) node).getFollowersList();
        }

        if(type == ObjectType.TWEET) {
            return ((Tweet) node).getRepostList();
        }

        return null;
    }

    private static String getParent(ObjectType type, DataModel node) {
        if(type == ObjectType.TWEET) {
            return ((Tweet) node).getAuthorProfileLink();
        }

        return null;
    }

    public static Graph createGraph(List<DataModel> userNodeList, List<DataModel> tweetNodeList) {
        Graph graph = new Graph();

        add(ObjectType.USER, graph, userNodeList, followWeight, 0.0);
        add(ObjectType.TWEET, graph, tweetNodeList, repostWeight, postWeight);

        return graph;
    }
}