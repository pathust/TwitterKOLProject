package model;

public abstract class DataModel {
    protected double pagerankScore;

    public double getPagerankScore() {
        return pagerankScore;
    }

    public void setPagerankScore(double pagerankScore) {
        this.pagerankScore = pagerankScore;
    }

    public abstract String getUniqueKey();

}
