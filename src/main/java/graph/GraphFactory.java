package graph;

import model.KOL;
import java.util.Map;

public class GraphFactory {
    public static Graph createGraph(Map<KOL, Map<KOL, Double>> kolInteractions) {
        Graph graph = new Graph();

        for (KOL kol : kolInteractions.keySet()) {
            graph.addNode(kol);
            for (KOL target : kolInteractions.get(kol).keySet()) {
                graph.addNode(target);
                double weight = kolInteractions.get(kol).get(target);
                graph.addEdge(kol, target, weight);
            }
        }

        return graph;
    }
}
