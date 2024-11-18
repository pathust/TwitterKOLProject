package graph;

import model.GraphNode;

import java.io.IOException;
import java.util.List;

public class MainTest {
    public static void main(String[] args) throws IOException {
        List<GraphNode> userNodeList = UserNodeExtract.extract();
        List<GraphNode> tweetNodeList = TweetNodeExtract.extract();

        Graph graph = GraphFactory.createGraph(userNodeList, tweetNodeList);

        PagerankCalculator.calculatePageRank(graph, 100);

        for(GraphNode node : userNodeList){
            System.out.println(node.getKol().getUsername() + " " + node.getPagerankScore());
        }
    }
}
