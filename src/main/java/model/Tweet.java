package model;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import static java.lang.Double.parseDouble;

public class Tweet {
    private String content;
    private String dateTime;
    private String tweetLink;
    private int repostCount;
    private String userLink;
    private List<String> repostList;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Tweet(String tweetLink, String userLink) {
        this.tweetLink = tweetLink;
        this.userLink = userLink;
    }

    public Tweet(String tweetLink, List<String> repostList) {
        this.tweetLink = tweetLink;
        this.repostList = repostList;
    }

    public static int toInt(String count) {
        int factor = 1;
        if (count.endsWith("K")) {
            count = count.replace("K", "");
            factor = 1000;
        } else if (count.endsWith("M")) {
            count = count.replace("M", "");
            factor = 1000_000;
        } else {
            count = count.replace(",", "");
        }

        // Parse to double to keep the decimal part, then cast to int after multiplying
        return (int) (parseDouble(count) * factor);
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

    public LocalDateTime getDateTime() {
        return LocalDateTime.parse(dateTime, formatter);
    }


    public void setDateTime(String dateTimeString) {
        this.dateTime = dateTimeString;
    }

    public List<String> getRepostList() {
        return repostList;
    }

    public void setRepostList(List<String> repostList) {
        this.repostList = repostList;
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }
}

