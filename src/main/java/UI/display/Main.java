package UI.display;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Tweet;
import model.User;
import storage.DataRepository;
import storage.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.List;

import static utils.ObjectType.USER;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Tạo danh sách KOL giả lập (dữ liệu mẫu)
        DataRepository dataRepository = new StorageHandler();
        try {
            dataRepository.load(USER, "KOLs.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<User> kolList = null;
        try {
            kolList = dataRepository.getAll(USER, "KOLs.json")
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