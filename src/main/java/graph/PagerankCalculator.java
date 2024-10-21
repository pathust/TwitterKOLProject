package graph;

import model.KOL;
import java.util.List;

public class PagerankCalculator {

    public void calculatePagerank(Graph graph, List<KOL> kolList) {
        for (KOL kol : kolList) {
            Double pagerankScore = (Math.random());
            kol.setPagerankScore(pagerankScore);
        }
    }
}
