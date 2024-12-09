package UI.display;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.Tweet;

import java.util.List;

public class TweetTableController extends TableController<Tweet> {

    @Override
    protected TableView<Tweet> createTable(ObservableList<Tweet> data) {
        TweetTable tweetTable = new TweetTable();
        return tweetTable.createTable(data);
    }

    @Override
    protected void showDetails(Tweet tweet) {
        Details details = new Details();
        details.showDetails(tweet); // Hiển thị thông tin chi tiết của Tweet
    }

    public VBox getTable(List<Tweet> tweetsList) {
        return super.getTable(tweetsList, Tweet::getAuthorUsername, Tweet::getTweetLink, Tweet::getAuthorProfileLink);
    }
}
