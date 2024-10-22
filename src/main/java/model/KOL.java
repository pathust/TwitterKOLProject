package model;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

public class KOL extends User{
    private Double pagerankScore;
    Map<KOL, Map<String, Double>> Interaction = new HashMap<>();

    public void setInteraction(Map<KOL, Map<String, Double>> Interaction){
        this.Interaction = Interaction;
    }

    public Map<KOL, Map<String, Double>> getInteraction(){
        return Interaction;
    }
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