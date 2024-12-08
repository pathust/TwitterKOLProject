package UI.display;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Tweet;

import java.util.List;

public class TweetTableController {
    public VBox getTable(List<Tweet> tweetsList) {
        // Dữ liệu chính
        ObservableList<Tweet> masterData = FXCollections.observableArrayList(tweetsList);

        // Lớp tạo bảng
        TweetTable tweetTable = new TweetTable();
        TableView<Tweet> tableView = tweetTable.createTable(masterData);

        // Lớp bộ lọc
        Filter tweetlFilter = new Filter();
        TextField searchField = new TextField();
        FilteredList<Tweet> filteredData = tweetlFilter.applyFilter(
                searchField,
                masterData,
                tableView,
                Tweet::getAuthorUsername,
                Tweet::getTweetLink,
                Tweet::getAuthorProfileLink
        );

        // Lớp hiển thị chi tiết
        Details tweetDetails = new Details();
        tweetTable.setupRowClickHandler(tableView, tweetDetails::showDetails);

        // Layout tổng thể
        VBox layout = new VBox(10);
        layout.getChildren().addAll(searchField, tableView);

        return layout;
    }
}
