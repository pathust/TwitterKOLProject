package scraper.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TwitterUserStorage {
    private final ObjectMapper mapper;
    private final Map<String, ObjectNode> userMap; // To store user data by username
    private final ArrayNode userArray;

    public TwitterUserStorage(String filePath) throws IOException {
        this.mapper = new ObjectMapper();
        this.userMap = new HashMap<>();
        this.userArray = mapper.createArrayNode();
        loadUsers(filePath);
    }

    private void loadUsers(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }
        JsonNode rootNode = mapper.readTree(file);
        if (rootNode.isArray()) {
            ArrayNode users = (ArrayNode) rootNode;
            for (JsonNode userNode : users) {
                if (userNode.has("profileLink")) {
                    String username = userNode.get("profileLink").asText();
                    userMap.put(username, (ObjectNode) userNode);
                }
            }
        }
    }

    public void addUser(User user) {
        ObjectNode userNode = userMap.get(user.getProfileLink());
        if (userNode != null) {
            updateUserFields(userNode, user);
        } else {
            userNode = createUserNode(user);
            userArray.add(userNode);
            userMap.put(user.getProfileLink(), userNode);
        }
    }

    public boolean userExists(String username) {
        return userMap.containsKey(username);
    }

    private void updateUserFields(ObjectNode userNode, User user) {
        if (!userNode.hasNonNull("isVerified") || !userNode.get("isVerified").asBoolean()) {
            userNode.put("isVerified", user.isVerified());
        }
        if (!userNode.hasNonNull("profileLink") || userNode.get("profileLink").asText().isEmpty()) {
            userNode.put("profileLink", user.getProfileLink());
        }
        if (!userNode.hasNonNull("followingList") || userNode.get("followingList").isEmpty()) {
            ArrayNode followingListNode = mapper.createArrayNode();
            user.getFollowingList().forEach(followingListNode::add);
            userNode.set("followingList", followingListNode);
        }
    }

    private ObjectNode createUserNode(User user) {
        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("username", user.getUsername());
        userNode.put("isVerified", user.isVerified());
        userNode.put("profileLink", user.getProfileLink());

        ArrayNode followingListNode = mapper.createArrayNode();
        user.getFollowingList().forEach(followingListNode::add);
        userNode.set("followingList", followingListNode);

        return userNode;
    }

    public void saveData(String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), userArray);
    }

    public List<String> getUserLinksFrom(String filePath) throws IOException {
        loadUsers(filePath);
        return userMap.values().stream()
                .map(userNode -> userNode.path("profileLink").asText())
                .filter(link -> !link.isEmpty())
                .collect(Collectors.toList());
    }

    public ArrayNode getUserArray() {
        return userArray;
    }
}
