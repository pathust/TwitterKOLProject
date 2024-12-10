package UI.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.Tweet;

import java.util.List;

public class TweetTableController extends TableController<Tweet> {

    @Override
    protected TableView<Tweet> createTable(ObservableList<Tweet> data) {
        TweetTable tweetTable = new TweetTable();
        return tweetTable.createTable(data);  // Sử dụng TweetTable để tạo bảng
    }

    @Override
    protected void showDetails(Tweet tweet) {
        Details details = new Details();
        details.showDetails(tweet);  // Hiển thị chi tiết của Tweet
    }

    // Tạo bảng với bộ lọc và thuộc tính cần lọc
    public VBox getTable(List<Tweet> tweetsList) {
        return super.getTable(tweetsList, Tweet::getAuthorUsername, Tweet::getTweetLink, Tweet::getAuthorProfileLink);
    }
}