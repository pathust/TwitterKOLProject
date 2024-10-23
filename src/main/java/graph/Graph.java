package graph;

import model.KOL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Map<KOL, Map<KOL, Double>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public Set<KOL> getNodes() {
        return graph.keySet();
    }

    public void addNode(KOL kol) {
        graph.putIfAbsent(kol, new HashMap<>());
    }

    public void addEdge(KOL kol, KOL target, Double weight) {
        graph.get(kol).put(target, weight);
    }

    public void addEdge(KOL kol) {
//        addNode(kol);
        Map<KOL, Double> Interaction = kol.getInteraction();

        for(KOL target : Interaction.keySet()) {
            //addNode(target);
            Double weight = Interaction.get(target);
            graph.get(kol).put(target, weight);
        }
    }

    public Map<KOL, Map<KOL, Double>> getGraph() {
        return graph;
    }
}
