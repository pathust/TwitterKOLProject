package scraper.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Tweet;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TweetStorage {
    private final ObjectMapper mapper;
    private final Map<String, ObjectNode> tweetMap;
    private final Map<String, Integer> tweetIndexMap;
    private final ArrayNode tweetArray;

    public TweetStorage(String filePath, boolean overwrite) throws IOException {
        this.mapper = new ObjectMapper();
        this.tweetMap = new HashMap<>();
        this.tweetIndexMap = new HashMap<>();
        this.tweetArray = mapper.createArrayNode();
        if (!overwrite)
            loadTweets(filePath);
    }

    private void loadTweets(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }
        JsonNode rootNode = mapper.readTree(file);
        if (rootNode.isArray()) {
            ArrayNode tweets = (ArrayNode) rootNode;
            int tweetIndex = 0;
            for (JsonNode tweetNode : tweets) {
                String identifier = createTweetIdentifier(tweetNode);
                tweetMap.put(identifier, (ObjectNode) tweetNode);
                tweetIndexMap.put(identifier, tweetIndex++);
                tweetArray.add(tweetNode);
            }
        }
    }

    private String createTweetIdentifier(JsonNode tweetNode) {
        String username = tweetNode.get("user").get("username").asText();
        String timestamp = tweetNode.get("timestamp").asText();
        return username + "_" + timestamp;
    }

    public void addTweet(Tweet tweet) {
        String identifier = tweet.getUser().getUsername() + "_" + tweet.getTimestamp();
        ObjectNode tweetNode = tweetMap.get(identifier);
        if (tweetNode != null) {
            updateTweetFields(tweetNode, tweet);
        } else {
            tweetNode = createTweetNode(tweet);
            int tweetIndex = tweetArray.size();
            tweetMap.put(identifier, tweetNode);
            tweetIndexMap.put(identifier, ++tweetIndex);
            tweetArray.add(tweetNode);
        }
    }

    private void updateTweetFields(ObjectNode tweetNode, Tweet tweet) {
        tweetNode.put("content", tweet.getContent());
        tweetNode.put("timestamp", tweet.getTimestamp().toString());

        ArrayNode repostedUsersNode = mapper.createArrayNode();
        for (User user : tweet.getRepostedUsersList()) {
            repostedUsersNode.add(user.getUsername());
        }
        tweetNode.set("repostedUsersList", repostedUsersNode);

        String identifier = createTweetIdentifier(tweetNode);
        int tweetIndex = tweetIndexMap.get(identifier);
        tweetArray.set(tweetIndex, tweetNode);
    }

    private ObjectNode createTweetNode(Tweet tweet) {
        ObjectNode tweetNode = mapper.createObjectNode();
        tweetNode.put("content", tweet.getContent());
        tweetNode.put("timestamp", tweet.getTimestamp().toString());

        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("username", tweet.getUser().getUsername());
        userNode.put("profileLink", tweet.getUser().getProfileLink());
        tweetNode.set("user", userNode);

        ArrayNode repostedUsersNode = mapper.createArrayNode();
        for (User user : tweet.getRepostedUsersList()) {
            repostedUsersNode.add(user.getUsername());
        }
        tweetNode.set("repostedUsersList", repostedUsersNode);

        return tweetNode;
    }

    public void saveData(String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), tweetArray);
    }

    public List<String> getTweetContents() {
        return tweetMap.values().stream()
                .map(tweetNode -> tweetNode.path("content").asText())
                .filter(content -> !content.isEmpty())
                .collect(Collectors.toList());
    }

    public ArrayNode getTweetArray() {
        return tweetArray;
    }
}
