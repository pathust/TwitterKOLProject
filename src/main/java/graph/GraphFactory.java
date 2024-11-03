package graph;

import model.GraphNode;
import model.User;

import java.util.List;
import java.util.Map;

import java.util.Random;

public class GraphFactory {
    public static Graph createGraph(List<GraphNode> nodeList) {
        Graph graph = new Graph();

        for (GraphNode node : nodeList) {
            graph.addNode(node);
        }

        for (GraphNode node : nodeList) {
            User userNode = node.getKol();
            List<User> followingList = userNode.getFollowingList();

            for(User target : followingList) {
                GraphNode targetNode = new GraphNode(target);
                graph.addEdge(node, targetNode, 1.0);

//                System.out.println(node.getKol().getUsername() + " " + target.getUsername());
            }
        }

        ///////// test add edge

        Random rand = new Random();
        for (GraphNode node : nodeList) {
            for (GraphNode otherNode : nodeList) {
                if(node != otherNode) {
                    if(rand.nextBoolean()) {
                        graph.addEdge(node, otherNode, 1.0);
                        System.out.println(node.getKol().getUsername() + " " + otherNode.getKol().getUsername());
                    }
                }
            }
        }


        return graph;
    }
}
