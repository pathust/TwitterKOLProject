package model;

import java.util.List;

public class KOL extends User{
    private Double pagerankScore;
    public KOL() {

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