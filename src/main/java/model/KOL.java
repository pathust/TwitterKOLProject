package model;

import java.util.HashMap;
import java.util.Map;

public class KOL extends User{
    private Double pagerankScore;
    Map<KOL, Double> Interaction = new HashMap<>();

    public KOL(String username, int followersCount) {
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

    public Map<KOL, Double> getInteraction() {
        return Interaction;
    }

    public void setInteraction(Map<KOL, Double> Interaction) {
        this.Interaction = Interaction;
    }

    public Double getTotalOutWeight() {
        Double TotalWeight = 0.0;
        for(KOL target : Interaction.keySet()) {
            Double weight = Interaction.get(target);
            TotalWeight += weight;
        }

        return TotalWeight;
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