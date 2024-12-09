package UI.table;

import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Tweet;

public class TweetTable extends TableCreate {
    public TableView<Tweet> createTable(ObservableList<Tweet> data) {
        TableView<Tweet> tableView = new TableView<>();

        // Tạo các cột bằng hàm chung
        TableColumn<Tweet, String> usernameCol = createColumn("Username", Tweet::getAuthorUsername);

        TableColumn<Tweet, String> profileLinkCol = createColumn("Profile Link", Tweet::getAuthorProfileLink);
        profileLinkCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Hyperlink link = new Hyperlink("Link");
                    link.setOnAction(e -> {
                        try {
                            java.awt.Desktop.getDesktop().browse(new java.net.URI(item));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    setGraphic(link);
                }
            }
        });

        TableColumn<Tweet, String> tweetLinkCol = createColumn("Tweet Link", Tweet::getTweetLink);
        tweetLinkCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Hyperlink link = new Hyperlink("Link");
                    link.setOnAction(e -> {
                        try {
                            java.awt.Desktop.getDesktop().browse(new java.net.URI(item));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    setGraphic(link);
                }
            }
        });

        TableColumn<Tweet, Integer> repostCountCol = createColumn("Repost Count", Tweet::getRepostCount);


        // Thêm các cột vào bảng
        tableView.getColumns().addAll(usernameCol, profileLinkCol, tweetLinkCol, repostCountCol);
        tableView.setItems(data);

        return tableView;
    }
}
