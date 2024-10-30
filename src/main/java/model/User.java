package model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class User {
    private String username;
    private String profileLink;
    private int followersCount;
    private boolean isVerified;
    private List<User> followingList;

    public User() {

    }

    public User(String username, String profileLink, boolean isVerified){
        this.username = username;
        this.profileLink = profileLink;
        this.followersCount = 0;
        this.isVerified = isVerified;
        this.followingList = new ArrayList<>();
    }

    public User(String profileLink, int followersCount, List<User> followingList) {
        this.username = null;
        this.profileLink = profileLink;
        this.followersCount = followersCount;
        this.isVerified = false;
        this.followingList = followingList;
    }

    public User(String username, String profileLink, boolean isVerified, int followersCount, List<User> followingList) {
        this.username = username;
        this.profileLink = profileLink;
        this.followersCount = followersCount;
        this.isVerified = isVerified;
        this.followingList = followingList;
    }

    public static int toInt(String followersCount) {
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

    public List<User> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<User> followingList) {
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
}
