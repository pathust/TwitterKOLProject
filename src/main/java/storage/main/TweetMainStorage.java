package storage.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetMainStorage extends MainStorage<Tweet> {

    @Override
    protected Tweet parseItem(JsonNode itemNode) {
        String authorUsername = itemNode.get("authorUsername").asText();
        String authorProfileLink = itemNode.get("authorProfileLink").asText();
        String tweetLink = itemNode.get("tweetLink").asText();
        int repostCount = itemNode.get("repostCount").asInt();

        Tweet tweet = new Tweet(tweetLink, authorProfileLink);
        tweet.setAuthorUsername(authorUsername);
        tweet.setRepostCount(repostCount);
        tweet.setRepostList(getRepostLinks(itemNode));
        return tweet;
    }

    @Override
    protected ObjectNode createItemNode(Tweet tweet) {
        ObjectNode tweetNode = mapper.createObjectNode();
        tweetNode.put("authorUsername", tweet.getAuthorUsername());
        tweetNode.put("authorProfileLink", tweet.getAuthorProfileLink());
        tweetNode.put("tweetLink", tweet.getTweetLink());
        tweetNode.put("repostCount", tweet.getRepostCount());
        tweetNode.set("repostList", createRepostLinksNode(tweet.getRepostList()));

        return tweetNode;
    }

    @Override
    protected void updateItemFields(ObjectNode tweetNode, Tweet tweet) {
        tweetNode.put("repostCount", tweet.getRepostCount());
        tweetNode.set("repostList", createRepostLinksNode(tweet.getRepostList()));
    }

    @Override
    protected String getIdentifier(Tweet tweet) {
        return tweet.getTweetLink();
    }

    private List<String> getRepostLinks(JsonNode tweetNode) {
        List<String> tweetLinks = new ArrayList<>();
        JsonNode listNode = tweetNode.path("repostList");
        if (listNode.isArray()) {
            for (JsonNode linkNode : listNode) {
                tweetLinks.add(linkNode.asText());
            }
        }
        return tweetLinks;
    }

    private ArrayNode createRepostLinksNode(List<String> tweetLinks) {
        ArrayNode repostListNode = mapper.createArrayNode();
        for (String tweetLink : tweetLinks) {
            repostListNode.add(tweetLink);
        }
        return repostListNode;
    }
}
