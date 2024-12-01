package model;

import java.util.HashMap;
import java.util.Map;

public class KOL extends User{
    Map<KOL, Double> Interaction = new HashMap<>();
    private Double pagerankScore;

    public KOL(String profileLink, String username){
        super(profileLink, username);
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

    public Double getWeightTo(KOL kol) {
        return Interaction.get(kol);
    }

    public Double getPagerankScore() {
        return pagerankScore;
    }

    public void setPagerankScore(Double pagerankScore) {
        this.pagerankScore = pagerankScore;
    }
}