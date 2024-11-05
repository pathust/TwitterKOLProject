package graph;

import model.GraphNode;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import java.util.Random;
import scraper.storage.UserDataHandler;
import scraper.storage.UserStorageManager;

public class GraphFactory {

    private final UserDataHandler userDataHandler;

    public GraphFactory() {
        this.userDataHandler = new UserStorageManager();
    }

    public User getUser(String filePath, String profileLink) throws IOException {
        return userDataHandler.getUser(filePath, profileLink);
    }

    public static Graph createGraph(List<GraphNode> nodeList) throws IOException {
        GraphFactory graphFactory = new GraphFactory();
        Graph graph = new Graph();

        for (GraphNode node : nodeList) {
            graph.addNode(node);
        }

        for (GraphNode node : nodeList) {
            User userNode = node.getKol();
            List<String> followingList = userNode.getFollowingList();

            for(String userLink : followingList) {
                GraphNode targetNode = new GraphNode(graphFactory.getUser("KOLs.json", userLink));
                graph.addEdge(node, targetNode, 1.0);

            }
        }

        ///////// test add edge

//        Random rand = new Random();
//        for (GraphNode node : nodeList) {
//            for (GraphNode otherNode : nodeList) {
//                if(node != otherNode) {
//                    if(rand.nextBoolean()) {
//                        graph.addEdge(node, otherNode, 1.0);
//                        System.out.println(node.getKol().getUsername() + " " + otherNode.getKol().getUsername());
//                    }
//                }
//            }
//        }


        return graph;
    }
}
