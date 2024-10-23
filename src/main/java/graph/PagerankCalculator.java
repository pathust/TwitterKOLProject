package graph;

import model.KOL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PagerankCalculator {
    private static final double DAMPING_FACTOR = 0.85;
    private static final double THRESHOLD = 0.0001;

    public static void calculatePageRank(Graph graph, int maxIterations) {
        Set<KOL> nodeList = graph.getNodes();
        int numNodes = nodeList.size();

        // initial all node's pagerank
        for(KOL node : nodeList)
            node.setPagerankScore(0.0);

        // Calculate PageRank
        while(maxIterations > 0) {
            double totalChange = 0.0;

            for(KOL node : nodeList) {
                double rankSum = 0.0;

                Map<KOL, Double> Interaction = node.getInteraction();
                for(KOL otherNode : Interaction.keySet()) {
                    double weight = 0.0;
                    if(otherNode.getWeightTo(node) != null)
                        weight = otherNode.getWeightTo(node);

                    double totalOutWeight = otherNode.getTotalOutWeight();
                    rankSum += otherNode.getPagerankScore() * (weight / totalOutWeight);
                }

                double newRank = (1 - DAMPING_FACTOR) / numNodes + DAMPING_FACTOR * rankSum;
                totalChange += Math.abs(newRank - node.getPagerankScore());
                node.setPagerankScore(newRank);
            }

            maxIterations--;

//             break if converge
            if(totalChange < THRESHOLD) {
                break;
            }
        }

    }
}
