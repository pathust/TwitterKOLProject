package model;

public class GraphNode {
    String nodeType;
    User kol;
    Tweet tweet;
    Double pagerankScore;

    public GraphNode(User kol) {
        nodeType = "KOL";
        this.kol = kol;
    }

    public GraphNode(Tweet tweet) {
        nodeType = "TWEET";
        this.tweet = tweet;
    }

    public String getNodeType() {
        return nodeType;
    }

    public User getKol() {
        return kol;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setPagerankScore(Double pagerankScore) {
        this.pagerankScore = pagerankScore;
    }

    public Double getPagerankScore() {
        return pagerankScore;
    }
}
