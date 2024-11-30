package UI.display;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import scraper.storage.UserStorage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        UserStorage handle = new UserStorage();
        try {
            handle.loadUsers("KOLs.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<User> kol = handle.getUsers();
        // Dữ liệu mẫu
        ObservableList<User> userList = FXCollections.observableArrayList(kol);

        // Tạo các lớp View và Controller
        TableView<User> tableView = new TableView<>();
        KOLTableView kolTableView = new KOLTableView();
        KOLTableController controller = new KOLTableController();

        // Tạo giao diện
        Scene scene = new Scene(kolTableView.createTableView(userList, tableView), 800, 600);

        // Cấu hình bộ lọc và sắp xếp
        controller.setupFilterAndSort(new TextField(), tableView, userList);

        primaryStage.setTitle("KOL Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}