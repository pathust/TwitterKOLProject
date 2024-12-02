package graph;

import model.GraphNode;

import java.util.*;

public class Graph {
    private Map<GraphNode, Map<GraphNode, Double>> graph;
    private Map<GraphNode, List<GraphNode>> inEdges;

    public Graph() {
        this.graph = new HashMap<>();
        this.inEdges = new HashMap<>();
    }

    public Set<GraphNode> getNodes() {
        return graph.keySet();
    }

    public void addNode(GraphNode node) {
        if(!graph.containsKey(node)) {
            graph.putIfAbsent(node, new HashMap<>());
            inEdges.putIfAbsent(node, new ArrayList<>());
        }
    }

    public void addEdge(GraphNode node, GraphNode targetNode, Double weight) {
        addNode(node);
        addNode(targetNode);
        graph.get(node).put(targetNode, weight);
        inEdges.get(targetNode).add(node);
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

    public List<GraphNode> getEdgeListTo(GraphNode node) {
        return inEdges.get(node);
    }

    public Map<GraphNode, Map<GraphNode, Double>> getGraph() {
        return graph;
    }
}
