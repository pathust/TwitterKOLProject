package model;

import java.time.LocalDateTime;
import java.util.List;

public class Tweet {
    private String tweetLink;
    private String content;
    private LocalDateTime timestamp;
    private Tweet user;
    private List<Tweet> repostedUsersList;
    private boolean isVerified;

    public Tweet(String tweetLink) {
        this.tweetLink= tweetLink;
        this.content = content;
        this.timestamp = timestamp;
        this.user = user;
        this.isVerified = false;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Tweet getUser() {
        return user;
    }

    public void setUser(Tweet user) {
        this.user = user;
    }

    public List<Tweet> getRepostedUsersList() {
        return repostedUsersList;
    }

    public void setRepostedUsersList(List<Tweet> repostedUsersList) {
        this.repostedUsersList = repostedUsersList;
    }
}
