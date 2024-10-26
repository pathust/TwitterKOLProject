package model;

import java.util.List;

public class KOL extends User{
    private Double pagerankScore;
    private List<String> KOLfollowers;
    private List<String> KOLfollowing;
    public KOL(String username, int followersCount, List<String> kolFollowersList, List<String> kolFollowingList) {
        super(username, followersCount);
    }

    public KOL(String username, int followersCount, Double pagerankScore) {
        super(username, followersCount);
        this.pagerankScore = pagerankScore;
    }

    public Double getPagerankScore() {
        return pagerankScore;
    }

    public void setPagerankScore(Double pagerankScore) {
        this.pagerankScore = pagerankScore;
    }

    @Override
    public String toString() {
        return "KOL{" +
                "username='" + getUsername() + '\'' +
                ", followersCount=" + getFollowersCount() +
                ", pagerankScore=" + pagerankScore +
                '}';
    }
}