package graph;

import model.GraphNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Map<GraphNode, Map<GraphNode, Double>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public Set<GraphNode> getNodes() {
        return graph.keySet();
    }

    public void addNode(GraphNode node) {
        if(!graph.containsKey(node)) {
            graph.putIfAbsent(node, new HashMap<>());
        }
    }

    public void addEdge(GraphNode node, GraphNode targetNode, Double weight) {
        addNode(node);
        graph.get(node).put(targetNode, weight);
    }

    public Double getWeight(GraphNode node, GraphNode targetNode) {
        Double weight = 0.0;
        if(graph.containsKey(node)) {
            if(graph.get(node).containsKey(targetNode)) {
                weight = graph.get(node).get(targetNode);
            }
        }

        return weight;
    }

    public Double getTotalOutweight(GraphNode node) {
        Double total = 0.0;
        for(GraphNode targetNode : graph.get(node).keySet()) {
            total += graph.get(node).get(targetNode);
        }

        return total;
    }

    public Map<GraphNode, Double> getEdgeListFrom(GraphNode node) {
        return graph.get(node);
    }

    public Map<GraphNode, Map<GraphNode, Double>> getGraph() {
        return graph;
    }
}
