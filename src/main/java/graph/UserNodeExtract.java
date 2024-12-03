package graph;

import model.DataModel;
import model.GraphNode;
import model.User;
import storage.DataRepository;
import storage.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserNodeExtract {
    public static List<GraphNode> extract() throws IOException {
        DataRepository dataRepository = new StorageHandler();
        dataRepository.load(ObjectType.USER, "KOLs.json");
        List<DataModel> userList = dataRepository.getAll(ObjectType.USER, "KOLs.json");

        List<GraphNode> nodeList = new ArrayList<>();

        for (DataModel dataModel : userList) {
            nodeList.add(new GraphNode((User) dataModel));
        }

        return nodeList;
    }

}
