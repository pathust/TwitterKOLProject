package UI.display;

import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import scraper.storage.UserStorageManager;

import java.io.IOException;
import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        UserStorageManager loaduser = new UserStorageManager();
        // Dữ liệu mẫu cho danh sách KOL
        List<User> kolList = null;
        try {
            loaduser.loadUsers("KOLs.json");
            kolList = loaduser.getUsers("KOLs.json");
            System.out.println("KOL List size: " + kolList.size());
        } catch (IOException e) {
            System.out.println("Can't load users from json");
        }

        // Tạo bảng hiển thị danh sách KOL
        KOLTableView kolTableView = new KOLTableView();
        kolTableView.displayTableView(stage, kolList);
    }

    public static void main(String[] args) {
        launch(args); // Khởi chạy ứng dụng JavaFX
    }
}