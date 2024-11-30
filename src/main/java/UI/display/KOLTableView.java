package UI.display;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class KOLTableView {

    public VBox createTableView(ObservableList<User> masterData, TableView<User> tableView) {
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

        // Layout bảng
        VBox vbox = new VBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        vbox.getChildren().addAll(new HBox(new Label("Search:"), searchField), tableView);
        return vbox;
    }

    public void showDetails(User user) {
        Stage stage = new Stage();
        stage.setTitle("Details");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Username: " + user.getUsername()),
                new Label("Profile Link: " + user.getProfileLink()),
                new Label("Followers Count: " + user.getFollowersCount()),
                new Label("Following Count: " + user.getFollowingCount())
        );

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}