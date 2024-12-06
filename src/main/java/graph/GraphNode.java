package graph;

import model.DataModel;
import model.Tweet;
import model.User;
import utils.ObjectType;

public class GraphNode {
    private ObjectType type;
    private DataModel dataModel;
    private Double pagerankScore;

    public GraphNode(ObjectType type, DataModel dataModel) {
        this.type = type;
        this.dataModel = dataModel;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public Double getPagerankScore() {
        return pagerankScore;
    }

    public void setPagerankScore(Double pagerankScore) {
        this.pagerankScore = pagerankScore;
    }
}
