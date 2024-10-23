package graph;

import model.KOL;

import java.util.HashMap;
import java.util.Map;

public class MainTest {
    public static void main(String[] args) {

        KOL A = new KOL("Anh",100);
        KOL B = new KOL("Bao",245);
        KOL C = new KOL("Cuong",300);
        KOL D = new KOL("Dung",430);

        Map<KOL, Map<KOL, Double>> kolInteractions = new HashMap<>();

        // example A
        Map<KOL, Double> aInteractions = new HashMap<>();
        aInteractions.put(B, 6.0);
        aInteractions.put(D, 5.0);
        aInteractions.put(C, 3.0);

        // example B
        Map<KOL, Double> bInteractions = new HashMap<>();
        bInteractions.put(A, 2.0);
        bInteractions.put(C, 1.0);
        bInteractions.put(D, 3.0);

        // example C
        Map<KOL, Double> cInteractions = new HashMap<>();
        cInteractions.put(A, 6.0);

        // example D
        Map<KOL, Double> dInteractions = new HashMap<>();
        dInteractions.put(B, 2.0);
        dInteractions.put(C, 1.0);
        dInteractions.put(A, 2.0);

        // Set Interaction

        A.setInteraction(aInteractions);
        B.setInteraction(bInteractions);
        C.setInteraction(cInteractions);
        D.setInteraction(dInteractions);

        // add to graph

        kolInteractions.put(A, aInteractions);
        kolInteractions.put(B, bInteractions);
        kolInteractions.put(C, cInteractions);
        kolInteractions.put(D, dInteractions);


        Graph graph = GraphFactory.createGraph(kolInteractions);

        PagerankCalculator.calculatePageRank(graph, 1);

        for(KOL kol : graph.getGraph().keySet())
            System.out.println(kol.getUsername() + " " + kol.getPagerankScore());
    }
}
