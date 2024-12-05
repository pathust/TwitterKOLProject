package UI.display;

import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import scraper.storage.UserStorage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Tạo danh sách KOL giả lập (dữ liệu mẫu)
        UserStorage handle = new UserStorage();
        try {
            handle.loadUsers("KOLs.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<User> kolList = handle.getUsers();

        // Sử dụng KOLTableController để khởi động ứng dụng
        KOLTableController controller = new KOLTableController();
        controller.start(primaryStage, kolList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}