package storage.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.DataModel;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserStorage extends Storage{

    @Override
    protected User parseItem(JsonNode itemNode) {
        String username = itemNode.get("username").asText();
        String profileLink = itemNode.get("profileLink").asText();
        int followersCount = itemNode.get("followersCount").asInt();
        int followingCount = itemNode.get("followingCount").asInt();

        User user = new User(profileLink, username);
        user.setFollowersCount(followersCount);
        user.setFollowingCount(followingCount);
        user.setFollowersList(getFollowersLinks(itemNode));
        return user;
    }

    @Override
    protected ObjectNode createItemNode(DataModel dataModel) {
        User user = (User) dataModel;
        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("username", user.getUsername());
        userNode.put("profileLink", user.getProfileLink());
        userNode.put("followersCount", user.getFollowersCount());
        userNode.put("followingCount", user.getFollowingCount());
        userNode.set("followersList", createFollowersLinksNode(user.getFollowersList()));
        return userNode;
    }

    @Override
    protected void updateItemFields(ObjectNode userNode, DataModel dataModel) {
        User user = (User) dataModel;
        userNode.put("followersCount", user.getFollowersCount());
        userNode.put("followingCount", user.getFollowingCount());
        userNode.set("followersList", createFollowersLinksNode(user.getFollowersList()));

        int userIndex = itemIndexMap.get(user.getProfileLink());
        itemArray.set(userIndex, userNode);
    }

    @Override
    protected String getIdentifier(DataModel dataModel) {
        User user = (User) dataModel;
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
