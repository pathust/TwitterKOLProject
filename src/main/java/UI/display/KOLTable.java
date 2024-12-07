package UI.display;

import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.User;

public class KOLTable extends TableCreate{
    public TableView<User> createTable(ObservableList<User> data) {
        TableView<User> tableView = new TableView<>();

        // Tạo các cột bằng hàm chung
        TableColumn<User, String> usernameCol = createColumn("Username", User::getUsername);

        TableColumn<User, String> profileLinkCol = createColumn("Profile Link", User::getProfileLink);
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

        TableColumn<User, Integer> followingCountCol = createColumn("Following Count", User::getFollowingCount);
        TableColumn<User, Integer> followersCountCol = createColumn("Followers Count", User::getFollowersCount);

        // Thêm các cột vào bảng
        tableView.getColumns().addAll(usernameCol, profileLinkCol, followingCountCol, followersCountCol);
        tableView.setItems(data);

        return tableView;
    }
}
