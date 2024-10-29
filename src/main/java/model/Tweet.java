package model;

import java.time.LocalDateTime;
import java.util.List;

public class Tweet {
    private String content;
    private LocalDateTime timestamp;
    private User user;
    private List<User> repostedUsersList;

    public Tweet(String content, LocalDateTime timestamp, User user) {
        this.content = content;
        this.timestamp = timestamp;
        this.user = user;
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
