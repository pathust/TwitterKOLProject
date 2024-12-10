package UI.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetTable extends Table<Tweet> {

    @Override
    protected List<TableColumn<Tweet, ?>> getColumns() {
        List<TableColumn<Tweet, ?>> columns = new ArrayList<>();

        // Cột Username
        TableColumn<Tweet, String> usernameCol = createColumn("Username", "authorUsername");

        // Cột Profile Link (dùng LinkCellFactory để xử lý các liên kết)
        TableColumn<Tweet, String> profileLinkCol = createColumn("Profile Link", "authorProfileLink");
        LinkCellFactory.applyLinkToColumn(profileLinkCol);

        // Cột Tweet Link (dùng LinkCellFactory để xử lý các liên kết)
        TableColumn<Tweet, String> tweetLinkCol = createColumn("Tweet Link", "tweetLink");
        LinkCellFactory.applyLinkToColumn(tweetLinkCol);

        // Cột Repost Count
        TableColumn<Tweet, Integer> repostCountCol = createColumn("Repost Count", "repostCount");

        columns.add(usernameCol);
        columns.add(profileLinkCol);
        columns.add(tweetLinkCol);
        columns.add(repostCountCol);

        return columns;
    }
}