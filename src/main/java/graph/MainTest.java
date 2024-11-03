package graph;

import model.GraphNode;

import java.io.IOException;
import java.util.List;

public class MainTest {
    public static void main(String[] args) throws IOException {
        List<GraphNode> nodeList = NodeExtract.extract();

        Graph graph = GraphFactory.createGraph(nodeList);

        PagerankCalculator.calculatePageRank(graph, 100);

        for(GraphNode node : nodeList){
            System.out.println(node.getKol().getUsername() + " " + node.getPagerankScore());
        }
    }
}
