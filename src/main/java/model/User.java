package model;

import java.util.List;

import static java.lang.Double.parseDouble;

public class User {
    private String username;
    private int followersCount;
    private List<String> followingList;

    public User(String username, int followersCount) {
        this.username = username;
        this.followersCount = followersCount;
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

    public static int toInt(String followersCount) {
        int factor = 1;
        if (followersCount.endsWith("K")) {
            followersCount = followersCount.replace("K", "");
            factor = 1000;
        } else if (followersCount.endsWith("M")) {
            followersCount = followersCount.replace("M", "");
            factor = 1000_000;
        }
        return (int)parseDouble(followersCount) * factor;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", followersCount=" + followersCount +
                '}';
    }
}
