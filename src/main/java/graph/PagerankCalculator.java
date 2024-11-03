package graph;

import model.GraphNode;

import java.util.Map;
import java.util.Set;

public class PagerankCalculator {
    private static final double DAMPING_FACTOR = 0.85;
    private static final double THRESHOLD = 0.0001;

    public static void calculatePageRank(Graph graph, int maxIterations) {
        Set<GraphNode> nodeList = graph.getNodes();
        int numNodes = nodeList.size();

        // initial all node's pagerank
        for(GraphNode node : nodeList)
            node.setPagerankScore(0.0);

        // Calculate PageRank
        while(maxIterations > 0) {
            double totalChange = 0.0;

            for(GraphNode node : nodeList) {
                double rankSum = 0.0;

                Map<GraphNode, Double> edgeList = graph.getEdgeListFrom(node);
                for(GraphNode otherNode : edgeList.keySet()) {
                    double weight = graph.getWeight(otherNode, node);
                    if(weight == 0.0)
                        continue;

                    double totalOutWeight = graph.getTotalOutweight(otherNode);
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
