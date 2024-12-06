package graph;

import utils.ObjectType;

import java.io.IOException;
import java.util.List;

public class MainTest {
    public static void main(String[] args) throws IOException {
        List<GraphNode> userNodeList = NodeExtract.extract(ObjectType.USER, "KOLs.json");
        List<GraphNode> tweetNodeList = NodeExtract.extract(ObjectType.TWEET, "Tweet.json");

        Graph graph = GraphFactory.createGraph(userNodeList, tweetNodeList);
        PagerankCalculator.calculatePageRank(graph, 100);

        for(GraphNode node : userNodeList){
            System.out.println(node.getType() + " " + node.getDataModel().getUniqueKey() + " " + node.getPagerankScore());
        }

        for(GraphNode node : tweetNodeList){
            System.out.println(node.getType() + " " + node.getDataModel().getUniqueKey() + " " + node.getPagerankScore());
        }
    }
}
