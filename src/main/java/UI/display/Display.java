package UI.display;

import UI.SwitchingScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import storage.DataRepository;
import storage.StorageHandler;

import java.io.IOException;
import java.util.List;

import static utils.ObjectType.USER;

// Class để quản lý giao diện và hiển thị bảng
public class Display {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    private FXMLLoader loader;
    private Button crawl, upload, staticData;
    private DataRepository dataRepository;
    private KOLTableController controller;
    private Parent root;

    public Display() {}

    public Display(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        loader = new FXMLLoader(getClass().getResource("/display.fxml"));
        root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        dataRepository = new StorageHandler();
//        try {
//            dataRepository.load(USER, "KOLs.json");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        kolList = null;
//        try {
//            kolList = dataRepository.getAll(USER, "KOLs.json")
//                    .stream()
//                    .filter(item -> item instanceof User)
//                    .map(item -> (User) item)
//                    .toList();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        controller = new KOLTableController();
//        VBox table = controller.getTable(kolList);
//
//        VBox vbox = (VBox) loader.getNamespace().get("Table");
//
//        vbox.getChildren().add(table);

        crawl = (Button) loader.getNamespace().get("Crawl");
        System.out.println(crawl);

        crawl.setOnAction(event -> {
            switchingScene.switchToSearching();
        });

        upload = (Button) loader.getNamespace().get("Upload");

        upload.setOnAction(event -> {
            System.out.println("Hello");
            switchingScene.switchToAddFile();
        });

        staticData = (Button) loader.getNamespace().get("Static");
        staticData.setOnAction(event -> {
            switchingScene.switchToDisplay();
        });

        scene = new Scene(root);
    }

    List<User> readDataFromKols() {
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

        return kolList;
    }

    private void init() {
        VBox table = controller.getTable(readDataFromKols());

        VBox vbox = (VBox) loader.getNamespace().get("Table");
//
        vbox.getChildren().clear();
//
        vbox.getChildren().add(table);

        scene.setRoot(root);
    }

    public void start() {
        init();
        stage.setScene(scene);
        stage.show();
    }
}
