package storage;

import model.DataModel;
import utils.ObjectType;

import java.io.IOException;
import java.util.List;

public interface DataRepository{
    void load(ObjectType type, String filePath) throws IOException;
    void add(ObjectType type, String filePath, DataModel item) throws IOException;
    void save(ObjectType type, String filePath) throws IOException;
    List<DataModel> getAll(ObjectType type, String filePath) throws IOException;
    boolean exists(ObjectType type, String filePath, String uniqueKey) throws IOException;
    DataModel get(ObjectType type, String filePath, String uniqueKey) throws IOException;
}
