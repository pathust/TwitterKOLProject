package scraper.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserStorage extends Storage<User> {

    @Override
    protected User parseItem(JsonNode itemNode) {
        String username = itemNode.get("username").asText();
        String profileLink = itemNode.get("profileLink").asText();
        boolean isVerified = itemNode.get("isVerified").asBoolean();
        int followersCount = itemNode.get("followersCount").asInt();
        int followingCount = itemNode.get("followingCount").asInt();

        User user = new User(profileLink, username, isVerified);
        user.setFollowersCount(followersCount);
        user.setFollowingCount(followingCount);
        user.setFollowersList(getFollowersLinks(itemNode));
        return user;
    }

    @Override
    protected ObjectNode createItemNode(User user) {
        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("username", user.getUsername());
        userNode.put("profileLink", user.getProfileLink());
        userNode.put("isVerified", user.isVerified());
        userNode.put("followersCount", user.getFollowersCount());
        userNode.put("followingCount", user.getFollowingCount());
        userNode.set("followersList", createFollowersLinksNode(user.getFollowersList()));
        return userNode;
    }

    @Override
    protected void updateItemFields(ObjectNode userNode, User user) {
        if (!userNode.get("isVerified").asBoolean()) {
            userNode.put("isVerified", user.isVerified());
        }
        userNode.put("followersCount", user.getFollowersCount());
        userNode.put("followingCount", user.getFollowingCount());
        userNode.set("followersList", createFollowersLinksNode(user.getFollowersList()));

        int userIndex = itemIndexMap.get(user.getProfileLink());
        itemArray.set(userIndex, userNode);
    }

    @Override
    protected String getIdentifier(User user) {
        return user.getProfileLink();
    }

    private List<String> getFollowersLinks(JsonNode userNode) {
        List<String> profileLinks = new ArrayList<>();
        JsonNode listNode = userNode.path("followersList");
        if (listNode.isArray()) {
            for (JsonNode linkNode : listNode) {
                profileLinks.add(linkNode.asText());
            }
        }
        return profileLinks;
    }

    private ArrayNode createFollowersLinksNode(List<String> userLinks) {
        ArrayNode followersListNode = mapper.createArrayNode();
        for (String userLink : userLinks) {
            followersListNode.add(userLink); // Chỉ lưu các link profile
        }
        return followersListNode;
    }
}
