package model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static java.lang.Double.parseDouble;

public class Tweet {
    private String content;
    private String dateTimeString;
    private LocalDateTime timestamp;
    private String tweetLink;
    private int repostCount;
    private User user;
    private List<User> repostedUsersList;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Tweet(String content, String dateTimeString, User user) {
        this.content = content;
        this.dateTimeString = dateTimeString;
        this.user = user;
    }


    public Tweet(String tweetLink,User user, int repostCount) {
        this.user = user;
        this.tweetLink = tweetLink;
        this.repostCount = repostCount;
    }

    public Tweet(String content, String dateTimeString,User user, String tweetLink,  int repostCount) {
        this.content = content;
        this.tweetLink = tweetLink;
        this.repostCount = repostCount;
        this.dateTimeString = dateTimeString;
        this.user = user;
    }

    public static Tweet fromJsonNode(ObjectNode node) {
        String content = node.get("content").asText();
        String timestamp = node.get("timestamp").asText();
        String username = node.get("user").get("username").asText();
        String tweetLink = node.get("user").get("tweetLink").asText();
        int repostCount = node.get("repostCount").asInt();
        User user = new User(username, tweetLink, true);
        return new Tweet(content, timestamp, user, tweetLink, repostCount);
    }


    public static int toInt(String repostCount) {
        int factor = 1;
        if (repostCount.endsWith("K")) {
            repostCount = repostCount.replace("K", "");
            factor = 1000;
        } else if (repostCount.endsWith("M")) {
            repostCount = repostCount.replace("M", "");
            factor = 1000000;
        } else {
            repostCount = repostCount.replace(",", "");
        }

        // Parse to double to keep the decimal part, then cast to int after multiplying
        return (int) (Double.parseDouble(repostCount) * factor);
    }


    public int getRepostCount(){
        return repostCount;
    }

    public void setRepostCount(int repostCount){
        this.repostCount = repostCount;
    }

    public String getTweetLink(){
        return tweetLink;
    }

    public void setTweetLink(String tweetLink) {
        this.tweetLink = tweetLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.parse(dateTimeString, formatter);
    }


    public void setTimestamp(String dateTimeString) {
        this.dateTimeString = dateTimeString;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getRepostedUsersList() {
        return repostedUsersList;
    }

    public void setRepostedUsersList(List<User> repostedUsersList) {
        this.repostedUsersList = repostedUsersList;
    }
}

