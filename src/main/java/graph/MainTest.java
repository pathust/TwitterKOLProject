package graph;

import model.DataModel;
import utils.ObjectType;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainTest {
    public static void main(String[] args) throws IOException {
        List<DataModel> userNodeList = NodeExtract.extract(ObjectType.USER, "KOLs.json");
        List<DataModel> tweetNodeList = NodeExtract.extract(ObjectType.TWEET, "Tweet.json");

        Graph graph = GraphFactory.createGraph(userNodeList, tweetNodeList);
        PagerankCalculator.calculatePageRank(graph, 100);

        for(DataModel node : userNodeList){
            System.out.println(node.getUniqueKey() + " " + node.getPagerankScore());
        }

        for(DataModel node : tweetNodeList){
            System.out.println(node.getUniqueKey() + " " + node.getPagerankScore());
        }
    }
}
