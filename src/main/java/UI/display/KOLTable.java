package UI.display;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import model.User;

public class KOLTable {

    public TableView<User> createTable(ObservableList<User> data) {
        TableView<User> tableView = new TableView<>();

        // Cột Username
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));

        // Cột Profile Link
        TableColumn<User, String> profileLinkCol = new TableColumn<>("Profile Link");
        profileLinkCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProfileLink()));
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

        // Cột Following Count
        TableColumn<User, Integer> followingCountCol = new TableColumn<>("Following Count");
        followingCountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFollowingCount()));

        // Cột Followers Count
        TableColumn<User, Integer> followersCountCol = new TableColumn<>("Followers Count");
        followersCountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getFollowersCount()));

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

