package graph;

import model.DataModel;

import java.util.*;

public class Graph {
    private Map<DataModel, Map<DataModel, Double>> graph;
    private Map<DataModel, List<DataModel>> inEdges;

    public Graph() {
        this.graph = new HashMap<>();
        this.inEdges = new HashMap<>();
    }

    public Set<DataModel> getNodes() {
        return graph.keySet();
    }

    public void addNode(DataModel node) {
        if(!graph.containsKey(node)) {
            graph.putIfAbsent(node, new HashMap<>());
            inEdges.putIfAbsent(node, new ArrayList<>());
        }
    }

    public void addEdge(DataModel node, DataModel targetNode, Double weight) {
        addNode(node);
        addNode(targetNode);
        graph.get(node).put(targetNode, weight);
        inEdges.get(targetNode).add(node);
    }

    public Double getWeight(DataModel node, DataModel targetNode) {
        Double weight = 0.0;
        if(graph.containsKey(node)) {
            if(graph.get(node).containsKey(targetNode)) {
                weight = graph.get(node).get(targetNode);
            }
        }

        return weight;
    }

    public Double getTotalOutweight(DataModel node) {
        Double total = 0.0;
        for(DataModel targetNode : graph.get(node).keySet()) {
            total += graph.get(node).get(targetNode);
        }

        return total;
    }

    public List<DataModel> getEdgeListTo(DataModel node) {
        return inEdges.get(node);
    }

    public Map<DataModel, Map<DataModel, Double>> getGraph() {
        return graph;
    }
}
