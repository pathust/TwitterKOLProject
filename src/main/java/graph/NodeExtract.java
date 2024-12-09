package graph;

import model.DataModel;
import model.User;
import storage.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodeExtract {
    public static List<DataModel> extract(ObjectType type, String filePath) throws IOException {
        StorageHandler storageHandler = new StorageHandler();
        storageHandler.load(type, filePath);
        List<DataModel> nodeList = storageHandler.getAll(type, filePath)
                .stream()
                .filter(Objects::nonNull)
                .map(item -> (DataModel) item)
                .toList();;
        return nodeList;
    }

}
