package scraper.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Tweet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetStorage {
    private final ObjectMapper mapper;
    private final Map<String, ObjectNode> objectNodeMap;
    private final Map<String, Integer> tweetIndexMap;
    private final Map<String, Tweet> tweetMap;
    private final ArrayNode tweetArray;

    public TweetStorage() {
        this.mapper = new ObjectMapper();
        this.objectNodeMap = new HashMap<>();
        this.tweetIndexMap = new HashMap<>();
        this.tweetMap = new HashMap<>();
        this.tweetArray = mapper.createArrayNode();
    }

    public void loadTweets(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        JsonNode rootNode = mapper.readTree(file);
        if (rootNode.isArray()) {
            ArrayNode tweets = (ArrayNode) rootNode;
            int tweetIndex = 0;

            for (JsonNode tweetNode : tweets) {
                String tweetLink = tweetNode.get("tweetLink").asText();
                String userLink = tweetNode.get("userLink").asText();
                int repostCount = tweetNode.get("repostCount").asInt();

                Tweet tweet = new Tweet(tweetLink, userLink, repostCount);
                tweet.setRepostCount(repostCount);
                tweet.setRepostList(getRepostLinks(tweetNode));

                objectNodeMap.put(tweetLink, (ObjectNode) tweetNode);
                tweetIndexMap.put(tweetLink, tweetIndex++);
                tweetMap.put(tweetLink, tweet);
                tweetArray.add(tweetNode);
            }

            System.out.println("Loaded " + tweetArray.size() + " tweets");
        }
    }

    public void addTweet(Tweet tweet) {
        if (tweetMap.containsKey(tweet.getTweetLink())) {
            updateTweet(tweet);
        } else {
            ObjectNode tweetNode = createTweetNode(tweet);
            int tweetIndex = tweetArray.size();

            objectNodeMap.put(tweet.getTweetLink(), tweetNode);
            tweetIndexMap.put(tweet.getTweetLink(), tweetIndex);
            tweetMap.put(tweet.getTweetLink(), tweet);
            tweetArray.add(tweetNode);
        }
    }

    public void saveData(String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), tweetArray);
    }

    public List<Tweet> getTweets() {
        List<Tweet> tweets = new ArrayList<>();
        for (JsonNode tweetNode : tweetArray) {
            String tweetLink = tweetNode.get("tweetLink").asText();
            tweets.add(tweetMap.get(tweetLink));
        }
        return tweets;
    }

    private ObjectNode createTweetNode(Tweet tweet) {
        ObjectNode tweetNode = mapper.createObjectNode();
        tweetNode.put("tweetLink", tweet.getTweetLink());
        tweetNode.put("userLink", tweet.getUserLink());
        tweetNode.put("repostCount", tweet.getRepostCount());

        // Kiểm tra nếu repostList là null, tạo một ArrayNode rỗng
        List<String> repostLinks = (tweet.getRepostList() != null) ? tweet.getRepostList() : new ArrayList<>();
        tweetNode.set("repostList", getRepostLinks(repostLinks));

        return tweetNode;
    }


    private void updateTweet(Tweet tweet) {
        ObjectNode tweetNode = objectNodeMap.get(tweet.getTweetLink());
        updateTweetFields(tweetNode, tweet);
        int tweetIndex = tweetIndexMap.get(tweet.getTweetLink());
        tweetArray.set(tweetIndex, tweetNode);
    }

    private void updateTweetFields(ObjectNode tweetNode, Tweet tweet) {
        tweetNode.put("repostCount", tweet.getRepostCount());
        tweetNode.set("repostList", getRepostLinks(tweet.getRepostList()));
    }

    private List<String> getRepostLinks(JsonNode tweetNode) {
        List<String> repostLinks = new ArrayList<>();
        JsonNode listNode = tweetNode.get("repostList");

        if (listNode.isArray()) {
            for (JsonNode linkNode : listNode) {
                repostLinks.add(linkNode.asText());
            }
        }
        return repostLinks;
    }

    private ArrayNode getRepostLinks(List<String> repostLinks) {
        ArrayNode repostLinkArray = mapper.createArrayNode();
        for (String repostLink : repostLinks) {
            repostLinkArray.add(repostLink);
        }
        return repostLinkArray;
    }

    public boolean tweetExists(String tweetLink) {
        return tweetMap.containsKey(tweetLink);
    }

    public Tweet getTweet(String tweetLink) {
        return tweetMap.get(tweetLink);
    }
}
