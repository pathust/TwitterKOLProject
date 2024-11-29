package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static utils.Math.toInt;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String username;
    private String profileLink;
    private int followersCount;
    private int followingCount;
    private List<String> followersList;

    public User() {
    }

    public User(String profileLink, String username) {
        this.profileLink = profileLink;
        this.username = username;
        this.followersCount = 0;
        this.followingCount = 0;
        this.followersList = new ArrayList<>();
    }

    public List<String> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(List<String> followersList) {
        this.followersList = followersList;
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

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }
}