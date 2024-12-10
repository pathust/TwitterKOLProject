package graph;

import model.DataModel;

import java.util.HashMap;
import java.util.Map;

public class GraphNodeStorage {
    private final Map<String, DataModel> graphNodeMap;

    public GraphNodeStorage() {
        graphNodeMap = new HashMap<>();
    }

    public void addNode(String uniqueKey, DataModel userNode) {
        graphNodeMap.put(uniqueKey, userNode);
    }

    public DataModel getNode(String uniqueKey) {
        return graphNodeMap.get(uniqueKey);
    }

    public boolean containsNode(String uniqueKey) {
        return graphNodeMap.containsKey(uniqueKey);
    }
}