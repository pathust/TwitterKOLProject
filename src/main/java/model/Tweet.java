package model;

import java.time.LocalDateTime;

public class Tweet {
    private String content;
    private LocalDateTime timestamp;
    private User user;

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

    @Override
    public String toString() {
        return "Tweet{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }
}
