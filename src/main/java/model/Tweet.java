package model;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Double.parseDouble;

public class Tweet {
    private String content;
    //private String dateTime;
    private String tweetLink;
    private int repostCount;
    private String userLink;
    private List<String> repostList;
    //private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Tweet(String tweetLink, String userLink,  int repostCount) {
        this.tweetLink = tweetLink;
        this.userLink = userLink;
        this.repostCount= repostCount;
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

    /*public LocalDateTime getDateTime() {
        if (dateTime == null || dateTime.isEmpty()) {
            System.out.println("Timestamp is null or empty, cannot parse.");
            return null;
        }
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("pattern_format"));
    }*/



    /*public void setDateTime(String dateTimeString) {
        this.dateTime = dateTimeString;
    }*/

    public List<String> getRepostList() {

        return repostList;
    }

    public void setRepostList(List<String> repostList) {
        this.repostList = repostList != null ? repostList : new ArrayList<>();
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }
}

