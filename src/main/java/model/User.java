package model;

public class User {
    private String username;
    private int followersCount;

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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", followersCount=" + followersCount +
                '}';
    }
}
