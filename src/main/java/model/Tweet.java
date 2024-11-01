package model;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static java.lang.Double.parseDouble;

public class Tweet {
    private String content;
    private String timestamp;
    private String tweetLink;
    private int repostCount;
    private String user;
    private List<User> repostedUsersList;

    public Tweet(String content, String timestamp, String user) {
        this.content = content;
        this.timestamp = timestamp;
        this.user = user;
    }

    public Tweet(String tweetLink, int repostCount, List<User> repostedUsersList) {
        this.user = null;
        this.tweetLink = tweetLink;
        this.repostCount = repostCount;
        this.repostedUsersList = repostedUsersList;
    }

    public Tweet(String content, String tweetLink,  int repostCount, List<User> repostedUsersList) {
        this.content = content;
        this.tweetLink = tweetLink;
        this.repostCount = repostCount;
        this.repostedUsersList = repostedUsersList;
    }

    public static Tweet fromJsonNode(ObjectNode node) {
        String username = node.get("user").get("username").asText();
        String timestamp = node.get("timestamp").asText();
        String content = node.get("content").asText();
        String tweetLink = node.get("user").get("tweetLink").asText();
        return new Tweet(content, timestamp, username);
    }

    public static int toInt(String repostCount) {
        int factor = 1;
        if (repostCount.endsWith("K")) {
            repostCount = repostCount.replace("K", "");
            factor = 1000;
        } else if (repostCount.endsWith("M")) {
            repostCount = repostCount.replace("M", "");
            factor = 1000_000;
        }
        else
            repostCount = repostCount.replace(",", "");
        return (int)parseDouble(repostCount) * factor;
    }

    public String getTweetLink(){
        return tweetLink;
    }

    public void setTweetLink(String profileLink) {
        this.tweetLink = tweetLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<User> getRepostedUsersList() {
        return repostedUsersList;
    }

    public void setRepostedUsersList(List<User> repostedUsersList) {
        this.repostedUsersList = repostedUsersList;
    }
}
