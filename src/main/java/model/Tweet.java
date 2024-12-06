package model;

import java.util.ArrayList;
import java.util.List;

public class Tweet extends DataModel{
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

    @Override
    public String getUniqueKey() {
        return tweetLink;
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

