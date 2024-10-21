package graph;

import model.KOL;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    private HashMap<KOL, HashSet<KOL>> followersGraph;

    public Graph() {
        this.followersGraph = new HashMap<>();
    }

    public void addNode(KOL kol) {
        followersGraph.putIfAbsent(kol, new HashSet<>());
    }

    public void addEdge(KOL follower, KOL following) {
        followersGraph.get(follower).add(following);
    }

    public HashMap<KOL, HashSet<KOL>> getFollowersGraph() {
        return followersGraph;
    }
}
