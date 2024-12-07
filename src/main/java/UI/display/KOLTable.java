package UI.display;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import model.User;

import java.util.function.Function;

public class KOLTable {

    // Hàm tạo cột chung
    public <T> TableColumn<User, T> createColumn(String title, Function<User, T> mapper) {
        TableColumn<User, T> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(mapper.apply(cellData.getValue()))
        );
        return column;
    }

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

    public void setupRowClickHandler(TableView<User> tableView, java.util.function.Consumer<User> onClickAction) {
        tableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    User clickedUser = row.getItem();
                    onClickAction.accept(clickedUser);
                }
            });
            return row;
        });
    }
}

