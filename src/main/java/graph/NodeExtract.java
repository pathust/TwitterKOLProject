package graph;

import model.DataModel;
import storage.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeExtract {
    public static List<DataModel> extract(ObjectType type, String filePath) throws IOException {
        StorageHandler storageHandler = new StorageHandler();
        storageHandler.load(type, filePath);
        List<DataModel> userList = storageHandler.getAll(type, filePath);

        List<DataModel> nodeList = new ArrayList<>();

        for (DataModel dataModel : userList) {
            nodeList.add(new DataModel() {
                @Override
                public String getUniqueKey() {
                    return "";
                }
            });
        }

        return nodeList;
    }

}
