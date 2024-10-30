package scraper.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserStorage {
    private final ObjectMapper mapper;
    private final Map<String, ObjectNode> userMap;
    private final Map<String, Integer> userIndexMap;
    private final ArrayNode userArray;

    public UserStorage(String filePath, boolean overwrite) {
        this.mapper = new ObjectMapper();
        this.userMap = new HashMap<>();
        this.userIndexMap = new HashMap<>();
        this.userArray = mapper.createArrayNode();
        if (!overwrite) {
            try {
                loadUsers(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadUsers(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }
        JsonNode rootNode = mapper.readTree(file);
        if (rootNode.isArray()) {
            ArrayNode users = (ArrayNode) rootNode;
            int userIndex = 0;
            for (JsonNode userNode : users) {
                String profileLink = userNode.get("profileLink").asText();
                userMap.put(profileLink, (ObjectNode) userNode);
                userIndexMap.put(profileLink, userIndex++);
                userArray.add(userNode);
            }
        }
    }

    public void addUser(User user) {
        ObjectNode userNode = userMap.get(user.getProfileLink());
        if (userNode != null) {
            updateUserFields(userNode, user);
        } else {
            userNode = createUserNode(user);
            int userIndex = userArray.size();
            userArray.add(userNode);
            userMap.put(user.getProfileLink(), userNode);
            userIndexMap.put(user.getProfileLink(), userIndex);
        }
    }

    public boolean userExists(String profileLink) {
        return userMap.containsKey(profileLink);
    }

    private void updateUserFields(ObjectNode userNode, User user) {
        if (!userNode.get("isVerified").asBoolean()) {
            userNode.put("isVerified", user.isVerified());
        }

        ArrayNode followingListNode = mapper.createArrayNode();
        for (User followingUser : user.getFollowingList()) {
            ObjectNode followingUserNode = mapper.createObjectNode();
            followingUserNode.put("username", followingUser.getUsername());
            followingUserNode.put("profileLink", followingUser.getProfileLink());
            followingListNode.add(followingUserNode);
        }
        userNode.set("followingList", followingListNode);

        int userIndex = userIndexMap.get(user.getProfileLink());
        userArray.set(userIndex, userNode);
    }


    private ObjectNode createUserNode(User user) {
        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("username", user.getUsername());
        userNode.put("isVerified", user.isVerified());
        userNode.put("profileLink", user.getProfileLink());

        ArrayNode followingListNode = mapper.createArrayNode();
        userNode.set("followingList", followingListNode);

        return userNode;
    }

    public List<User> getFollowingList(User user) {
        List<User> followingUsers = new ArrayList<>();

        ObjectNode userNode = userMap.get(user.getProfileLink());
        if (userNode == null) {
            return followingUsers;
        }

        ArrayNode followingArrayNode = (ArrayNode) userNode.path("followingList");
        for (JsonNode followingNode : followingArrayNode) {
            String followingUsername = followingNode.path("username").asText();
            String followingProfileLink = followingNode.path("profileLink").asText();
            boolean isVerified = followingNode.path("isVerified").asBoolean();

            User followingUser = new User(followingUsername, followingProfileLink, isVerified);
            followingUsers.add(followingUser);
        }

        return followingUsers;
    }



    public void saveData(String filePath) throws IOException {
        File file = new File(filePath);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, userArray);
    }

    public List<String> getUserLinks() {
        return userMap.values().stream()
                .map(userNode -> userNode.path("profileLink").asText())
                .filter(link -> !link.isEmpty())
                .collect(Collectors.toList());
    }

    public ArrayNode getUserArray() {
        return userArray;
    }
}
