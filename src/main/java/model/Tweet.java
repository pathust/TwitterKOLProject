package model;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Double.parseDouble;

public class Tweet {
    private String authorUsername;
    private String authorProfileLink;
    private String tweetLink;
    private String content;
    private int likeCount;
    private int commentCount;
    private int repostCount;
    private int viewCount;
    private List<String> repostList;
    //private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Tweet(String tweetLink, String authorProfileLink,  int repostCount) {
        this.tweetLink = tweetLink;
        this.authorProfileLink = authorProfileLink;
        this.repostCount= repostCount;
        repostList = new ArrayList<>();
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
        this.repostList =
                (repostList != null)
                        ? repostList
                        : new ArrayList<>();
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorProfileLink() {
        return authorProfileLink;
    }

    public void setAuthorProfileLink(String authorProfileLink) {
        this.authorProfileLink = authorProfileLink;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}

