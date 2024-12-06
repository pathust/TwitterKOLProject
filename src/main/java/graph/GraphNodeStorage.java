package graph;

import java.util.HashMap;
import java.util.Map;

public class GraphNodeStorage {
    private final Map<String, GraphNode> graphNodeMap;

    public GraphNodeStorage() {
        graphNodeMap = new HashMap<>();
    }

    public void addNode(String uniqueKey, GraphNode userNode) {
        graphNodeMap.put(uniqueKey, userNode);
    }

    public GraphNode getNode(String uniqueKey) {
        return graphNodeMap.get(uniqueKey);
    }
}
