package graph;

import model.DataModel;
import model.GraphNode;
import model.Tweet;
import storage.DataRepository;
import storage.StorageHandler;
import utils.ObjectType;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TweetNodeExtract {
    public static List<GraphNode> extract() throws IOException {
        DataRepository dataRepository = new StorageHandler();
        dataRepository.load(ObjectType.TWEET, "Tweet.json");
        List<DataModel> tweetList = dataRepository.getAll(ObjectType.TWEET, "Tweet.json");

        List<GraphNode> nodeList = new ArrayList<>();

        for (DataModel dataModel : tweetList) {
           nodeList.add(new GraphNode((Tweet) dataModel));
        }

        return nodeList;
    }
}
