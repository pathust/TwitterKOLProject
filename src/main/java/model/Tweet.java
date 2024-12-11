package model;

import java.util.ArrayList;
import java.util.List;

public class Tweet extends DataModel{
    private String tweetLink;
    private String authorProfileLink;
    private String authorUsername;
    private String content;
    private int likeCount;
    private int commentCount;
    private int repostCount;
    private List<String> repostList;

    public Tweet(String tweetLink, String authorProfileLink) {
        this.tweetLink = tweetLink;
        this.authorProfileLink = authorProfileLink;
        repostList = new ArrayList<>();
    }

    public Tweet(String tweetLink, String authorProfileLink, String authorUsername, String content,
                 int likeCount, int commentCount, int repostCount) {
        this.tweetLink = tweetLink;
        this.authorProfileLink = authorProfileLink;
        this.authorUsername = authorUsername;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.repostCount = repostCount;
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
        this.repostList = (repostList != null) ? repostList : new ArrayList<>();
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
}

