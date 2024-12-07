package UI.display;

import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import storage.StorageHandler;

import java.io.IOException;
import java.util.List;

import static utils.ObjectType.USER;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Tạo danh sách KOL giả lập (dữ liệu mẫu)
        StorageHandler storageHandler = new StorageHandler();
        try {
            storageHandler.load(USER, "KOLs.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<User> kolList = null;
        try {
            kolList = storageHandler.getAll(USER, "KOLs.json")
                    .stream()
                    .filter(item -> item instanceof User)
                    .map(item -> (User) item)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sử dụng KOLTableController để khởi động ứng dụng
        KOLTableController controller = new KOLTableController();
        controller.start(primaryStage, kolList);
    }

    public static void main(String[] args) {
        launch(args);
    }
}