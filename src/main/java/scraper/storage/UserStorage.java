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

public class UserStorage {
    private final ObjectMapper mapper;
    private final Map<String, ObjectNode> objectNodeMap;
    private final Map<String, Integer> userIndexMap;
    private final Map<String, User> userMap;

    private final ArrayNode userArray;

    public UserStorage() {
        this.mapper = new ObjectMapper();
        this.objectNodeMap = new HashMap<>();
        this.userIndexMap = new HashMap<>();
        this.userMap = new HashMap<>();
        this.userArray = mapper.createArrayNode();
    }

    public void loadUsers(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }
        JsonNode rootNode = mapper.readTree(file);
        if (rootNode.isArray()) {
            ArrayNode users = (ArrayNode) rootNode;
            int userIndex = 0;
            for (JsonNode userNode : users) {
                String username = userNode.get("username").asText();
                String profileLink = userNode.get("profileLink").asText();
                boolean isVerified = userNode.get("isVerified").asBoolean();
                int followersCount = userNode.get("followersCount").asInt();
                int followingCount = userNode.get("followingCount").asInt();

                User user = new User(profileLink, username, isVerified);
                user.setFollowersCount(followersCount);
                user.setFollowingCount(followingCount);
                user.setFollowingList(getFollowingLinks(userNode));

                objectNodeMap.put(profileLink, (ObjectNode) userNode);
                userIndexMap.put(profileLink, userIndex++);
                userMap.put(profileLink, user);
                userArray.add(userNode);
            }

            System.out.println("Loaded " + userArray.size() + " users");
        }
    }

    public void addUser(User user) {
        ObjectNode userNode = objectNodeMap.get(user.getProfileLink());
        if (userNode != null) {
            updateUserFields(userNode, user);
        } else {
            userNode = createUserNode(user);
            int userIndex = userArray.size();

            objectNodeMap.put(user.getProfileLink(), userNode);
            userIndexMap.put(user.getProfileLink(), userIndex);
            userMap.put(user.getProfileLink(), user);
            userArray.add(userNode);
        }
    }

    public void saveData(String filePath) throws IOException {
        File file = new File(filePath);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, userArray);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (JsonNode userNode : userArray) {
            String profileLink = userNode.get("profileLink").asText();
            User user = userMap.get(profileLink);
            users.add(user);
        }
        return users;
    }

    private ObjectNode createUserNode(User user) {
        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("username", user.getUsername());
        userNode.put("profileLink", user.getProfileLink());
        userNode.put("isVerified", user.isVerified());
        userNode.put("followersCount", user.getFollowersCount());
        userNode.put("followingCount", user.getFollowingCount());
        userNode.set("followingList", getFollowingLinks(user.getFollowingList()));
        return userNode;
    }

    private void updateUserFields(ObjectNode userNode, User user) {
        if (!userNode.get("isVerified").asBoolean()) {
            userNode.put("isVerified", user.isVerified());
        }
        userNode.put("followersCount", user.getFollowersCount());
        userNode.put("followingCount", user.getFollowingCount());
        userNode.set("followingList", getFollowingLinks(user.getFollowingList()));

        int userIndex = userIndexMap.get(user.getProfileLink());
        userArray.set(userIndex, userNode);
    }

    private List<String> getFollowingLinks(JsonNode userNode) {
        List <String> profileLinks = new ArrayList<>();
        JsonNode listNode = userNode.path("followingList");
        if (listNode.isArray()) {
            for (JsonNode linkNode : listNode) {
                String profileLink = linkNode.asText();
                profileLinks.add(profileLink);
            }
        }

        return profileLinks;
    }

    private ArrayNode getFollowingLinks(List<String> userLinks) {
        ArrayNode followingListNode = mapper.createArrayNode();
        for (String userLink : userLinks) {
            followingListNode.add(userLink); // Add only the profile link as a string
        }
        return followingListNode;
    }

    public boolean userExists(String profileLink) {
        return userMap.containsKey(profileLink);
    }

    public User getUser(String profileLink) {
        return userMap.get(profileLink);
    }
}
