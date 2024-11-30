package scraper.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.DataModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Storage {
    protected final ObjectMapper mapper;
    protected final Map<String, ObjectNode> objectNodeMap;
    protected final Map<String, Integer> itemIndexMap;
    protected final Map<String, DataModel> itemMap;
    protected final ArrayNode itemArray;

    protected abstract DataModel parseItem(JsonNode itemNode);

    protected abstract ObjectNode createItemNode(DataModel item);

    protected abstract void updateItemFields(ObjectNode itemNode, DataModel item);

    protected abstract String getIdentifier(DataModel item);

    public Storage() {
        this.mapper = new ObjectMapper();
        this.objectNodeMap = new HashMap<>();
        this.itemIndexMap = new HashMap<>();
        this.itemMap = new HashMap<>();
        this.itemArray = mapper.createArrayNode();
    }

    public void load(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) return;

        JsonNode rootNode = mapper.readTree(file);
        if (rootNode.isArray()) {
//            ArrayNode users = (ArrayNode) rootNode;
            int itemIndex = 0;
            for (JsonNode itemNode : rootNode) {
                DataModel item = parseItem(itemNode);
                String identifier = getIdentifier(item);

                objectNodeMap.put(identifier, (ObjectNode) itemNode);
                itemIndexMap.put(identifier, itemIndex++);
                itemMap.put(identifier, item);
                itemArray.add(itemNode);
            }
        }
    }

    public void save(String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), itemArray);
    }

    public void add(DataModel item) {
        String identifier = getIdentifier(item);
        ObjectNode itemNode = objectNodeMap.get(identifier);
        if (itemNode != null) {
            updateItemFields(itemNode, item);
        } else {
            itemNode = createItemNode(item);
            int itemIndex = itemArray.size();

            objectNodeMap.put(identifier, itemNode);
            itemIndexMap.put(identifier, itemIndex);
            itemMap.put(identifier, item);
            itemArray.add(itemNode);
        }
    }

    public boolean exists(String identifier) {
        return itemMap.containsKey(identifier);
    }

    public DataModel get(String identifier) {
        return itemMap.get(identifier);
    }

    public List<DataModel> getAll() {
        return new ArrayList<>(itemMap.values());
    }
}
