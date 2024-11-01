package scraper.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Tweet;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        // Kiểm tra xem tweet đã có trong tweetMap chưa
        ObjectNode tweetNode = tweetMap.get(tweet.getTweetLink());
        if (tweetNode != null) {
            updateTweetFields(tweetNode, tweet);
        } else {
            // Tạo một tweetNode mới nếu chưa có
            tweetNode = createTweetNode(tweet);
            int tweetIndex = tweetArray.size();
            tweetArray.add(tweetNode);
            tweetIndexMap.put(tweet.getTweetLink(), tweetIndex);
            tweetMap.put(tweet.getTweetLink(), tweetNode);
        }
    }
/*
    private List<Tweet> getTweetsByLinks(List<String> tweetLinks) {
        List<Tweet> tweets = new ArrayList<>();
        for (String tweetLink :tweetLinks) {
            ObjectNode tweet = tweetMap.get(tweetLink);
            tweets.add(tweet);
        }
        return tweets;
    }*/
private List<Tweet> getTweetsByLinks(List<String> tweetLinks) {
    List<Tweet> tweets = new ArrayList<>();
    for (String tweetLink : tweetLinks) {
        ObjectNode tweetNode = tweetMap.get(tweetLink);
        if (tweetNode != null) {
            Tweet tweet = Tweet.fromJsonNode(tweetNode); // Chuyển đổi ObjectNode sang Tweet
            tweets.add(tweet);
        }
    }
    return tweets;
}


    public List<Tweet> getTweets() {
        List<Tweet> tweets = new ArrayList<>();
        for (JsonNode tweetNode : tweetArray) {
            if (tweetNode instanceof ObjectNode) {
                String tweetLink = tweetNode.get("user").get("tweetLink").asText();
                // Chuyển đổi từ ObjectNode sang Tweet
                Tweet tweet = Tweet.fromJsonNode((ObjectNode) tweetNode);
                tweets.add(tweet);
            }
        }
        return tweets;
    }


    private List<Tweet> getTweets(JsonNode userNode) {
        List <String> tweetLinks = new ArrayList<>();
        JsonNode listNode = userNode.path("repostList");
        if (listNode.isArray()) {
            for (JsonNode linkNode : listNode) {
                String tweetLink = linkNode.path("tweetLink").asText();
                tweetLinks.add(tweetLink);
            }
        }

        return getTweetsByLinks(tweetLinks);
    }

    private void updateTweetFields(ObjectNode tweetNode, Tweet tweet) {
        tweetNode.put("content", tweet.getContent());
        tweetNode.put("timestamp", tweet.getTimestamp().toString());

        ArrayNode repostedUsersNode = mapper.createArrayNode();
        List<User> repostedUsers = tweet.getRepostedUsersList();
        if(repostedUsers != null) {
            for (User user : repostedUsers) {
                repostedUsersNode.add(user.getUsername());
            }
            tweetNode.set("repostedUsersList", repostedUsersNode);

            String identifier = createTweetIdentifier(tweetNode);
            int tweetIndex = tweetIndexMap.get(identifier);
            tweetArray.set(tweetIndex, tweetNode);
        }
        else{
            System.out.println("RepostedUsersList is empty.");
        }
    }



    private ObjectNode createTweetNode(Tweet tweet) {
        ObjectNode tweetNode = mapper.createObjectNode();
        tweetNode.put("content", tweet.getContent());
        tweetNode.put("timestamp", tweet.getTimestamp().toString());

        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("username", tweet.getUser());
        userNode.put("tweetLink", tweet.getTweetLink());
        tweetNode.set("user", userNode);
/*
        ArrayNode repostedUsersNode = mapper.createArrayNode();
        for (User user : tweet.getRepostedUsersList()) {
            repostedUsersNode.add(user.getUsername());
        }
        tweetNode.set("repostedUsersList", repostedUsersNode);
*/
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
