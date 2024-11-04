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

    public TweetStorage() throws IOException {
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
                String dateTime = tweetNode.get("dateTime").asText();
                String content = tweetNode.get("content").asText();
                int repostCount = tweetNode.get("repostCount").asInt();

                Tweet tweet = new Tweet(tweetLink, userLink);
                tweet.setContent(content);
                tweet.setDateTime(dateTime);
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
        ObjectNode tweetNode = objectNodeMap.get(tweet.getTweetLink());
        if (tweetNode != null) {
            updateTweetFields(tweetNode, tweet);
        } else {
            tweetNode = createTweetNode(tweet);
            int tweetIndex = tweetArray.size();

            objectNodeMap.put(tweet.getTweetLink(), tweetNode);
            tweetIndexMap.put(tweet.getTweetLink(), tweetIndex);
            tweetMap.put(tweet.getTweetLink(), tweet);
            tweetArray.add(tweetNode);
        }
    }

    public void saveData(String filePath) throws IOException {
        File file = new File(filePath);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, tweetArray);
    }

    public List<Tweet> getTweets() {
        List<Tweet> tweets = new ArrayList<>();
        for (JsonNode tweetNode : tweetArray) {
            String tweetLink = tweetNode.get("repostList").asText();
            Tweet tweet = tweetMap.get(tweetLink);
            tweets.add(tweet);
        }
        return tweets;
    }

    private ObjectNode createTweetNode(Tweet tweet) {
        ObjectNode tweetNode = mapper.createObjectNode();

        tweetNode.put("tweetLink", tweet.getTweetLink());
        tweetNode.put("userLink", tweet.getUserLink());
        tweetNode.put("dateTime", tweet.getDateTime().toString());
        tweetNode.put("content", tweet.getContent());
        tweetNode.put("repostCount", tweet.getRepostCount());
        tweetNode.set("repostList", getRepostLinks(tweet.getRepostList()));
        return tweetNode;
    }

    private void updateTweetFields(ObjectNode tweetNode, Tweet tweet) {
        tweetNode.put("dateTime", tweet.getDateTime().toString());
        tweetNode.put("content", tweet.getContent());
        tweetNode.put("repostCount", tweet.getRepostCount());
        tweetNode.set("repostList",getRepostLinks(tweet.getRepostList()));
        int tweetIndex = tweetIndexMap.get(tweet.getTweetLink());
        tweetArray.set(tweetIndex, tweetNode);
    }

    private List<String> getRepostLinks(JsonNode tweetNode) {
        List<String> repostLinks = new ArrayList<>();
        JsonNode listNode = tweetNode.get("repostList");
        if (listNode.isArray()) {
            for (JsonNode linkNode : listNode) {
                String repostLink = linkNode.asText();
                repostLinks.add(repostLink);
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


    public ArrayNode getTweetArray() {
        return tweetArray;
    }
}

