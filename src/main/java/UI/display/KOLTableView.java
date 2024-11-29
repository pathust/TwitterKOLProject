package UI.display;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


public class KOLTableView {

    // Hàm tạo và hiển thị bảng
    public TableView<User> createTableView(List<User> kolList) {
        TableView<User> tableView = new TableView<>();

        // Cột Username
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Cột Profile Link
        TableColumn<User, String> profileLinkCol = new TableColumn<>("Profile Link");
        profileLinkCol.setCellValueFactory(new PropertyValueFactory<>("profileLink"));
        profileLinkCol.setCellFactory(col -> {
            return new TableCell<User, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        Hyperlink link = new Hyperlink("Link");
                        link.setOnAction(e -> {
                            try {
                                Desktop.getDesktop().browse(new URI(item)); // Mở liên kết trong trình duyệt mặc định
                            } catch (IOException | URISyntaxException ex) {
                                ex.printStackTrace();
                            }
                        });
                        setGraphic(link);
                    }
                }
            };
        });

        // Cột Following Count
        TableColumn<User, Integer> followingCountCol = new TableColumn<>("Following Count");
        followingCountCol.setCellValueFactory(new PropertyValueFactory<>("followingCount"));

        // Cột Followers Count
        TableColumn<User, Integer> followersCountCol = new TableColumn<>("Followers Count");
        followersCountCol.setCellValueFactory(new PropertyValueFactory<>("followersCount"));

        // Thêm các cột vào bảng
        tableView.getColumns().addAll(usernameCol, profileLinkCol, followingCountCol, followersCountCol);

        // Khi click vào 1 dòng, hiển thị chi tiết
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                User selectedUser = tableView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    showUserDetails(selectedUser);  // Hiển thị chi tiết thông tin người dùng
                }
            }
        });

        tableView.setItems(javafx.collections.FXCollections.observableArrayList(kolList));

        return tableView;
    }

    private void showUserDetails(User user) {
        Stage detailStage = new Stage();
        detailStage.setTitle("User Details");

        // Tạo Label cho các thuộc tính của User
        Label usernameLabel = new Label("Username: " + user.getUsername());

        // Sử dụng Text thay vì Label cho các thuộc tính dài
        Text profileLinkText = new Text("Profile Link: " + user.getProfileLink());
        Text followingCountText = new Text("Following Count: " + user.getFollowingCount());
        Text followersCountText = new Text("Followers Count: " + user.getFollowersCount());

        // Đảm bảo Text có thể xuống dòng nếu cần thiết
        profileLinkText.setWrappingWidth(400);
        followingCountText.setWrappingWidth(400);
        followersCountText.setWrappingWidth(400);

        // Đặt tất cả các thành phần vào một VBox (hoặc HBox nếu bạn muốn theo chiều ngang)
        VBox vbox = new VBox(10, usernameLabel, profileLinkText, followingCountText, followersCountText);
        vbox.setSpacing(10);

        // Đặt VBox vào trong một ScrollPane nếu nội dung quá dài
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vbox);
        scrollPane.setFitToWidth(true);  // Đảm bảo ScrollPane vừa với chiều rộng cửa sổ

        // Tạo Scene và hiển thị
        Scene scene = new Scene(scrollPane, 400, 300);
        detailStage.setScene(scene);
        detailStage.show();
    }

    // Hàm hiển thị bảng
    public void displayTableView(Stage stage, List<User> kolList) {
        TableView<User> tableView = createTableView(kolList);

        // Tạo cảnh cho stage
        Scene scene = new Scene(tableView, 600, 400);
        stage.setTitle("KOL Table View");
        stage.setScene(scene);
        stage.show();
    }
}