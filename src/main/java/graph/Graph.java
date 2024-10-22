package graph;

import model.KOL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Graph {
    private Map<KOL, Map<KOL, Map<String, Double>>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public void addNode(KOL kol) {
        graph.putIfAbsent(kol, new HashMap<>());
    }

    public void addEdge(KOL kol) {
        addNode(kol);
        Map<KOL, Map<String, Double>> Interaction = kol.getInteraction();

        for(KOL target : Interaction.keySet()) {
            //addNode(target);
            for (String interactionType : Interaction.get(target).keySet()) {
                Double weight = Interaction.get(target).get(interactionType);
                graph.get(kol).put(target, new HashMap<>());
                graph.get(kol).get(target).put(interactionType, weight);
            }
        }
    }

    public Map<KOL, Map<KOL, Map<String, Double>>> getFollowersGraph() {
        return graph;
    }
}
