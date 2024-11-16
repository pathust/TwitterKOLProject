package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String username;
    private String profileLink;
    private int followersCount;
    private int followingCount;
    private boolean isVerified;
    private List<String> followingList;

    public User() {
    }

    public User(String profileLink, String username, boolean isVerified){
        this.profileLink = profileLink;
        this.username = username;
        this.followersCount = 0;
        this.followingCount = 0;
        this.isVerified = isVerified;
        this.followingList = new ArrayList<>();
    }

    private int toInt(String followersCount) {
        int factor = 1;
        if (followersCount.endsWith("K")) {
            followersCount = followersCount.replace("K", "");
            factor = 1000;
        } else if (followersCount.endsWith("M")) {
            followersCount = followersCount.replace("M", "");
            factor = 1000_000;
        }
        else
            followersCount = followersCount.replace(",", "");
        return (int)parseDouble(followersCount) * factor;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<String> followingList) {
        this.followingList = followingList;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = toInt(followersCount);
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = toInt(followingCount);
    }
}
