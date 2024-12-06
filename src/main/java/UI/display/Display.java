package UI.display;

import UI.SwitchingScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import scraper.storage.UserStorage;

import java.io.IOException;
import java.util.List;

// Class để quản lý giao diện và hiển thị bảng
public class Display {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    private FXMLLoader loader;
    private Button crawl, upload, staticData;
    private Parent root;
    private UserStorage handle;
    private KOLTableController controller;

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

        handle = new UserStorage();
        try {
            handle.loadUsers("KOLs.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controller = new KOLTableController();

        crawl = (Button) loader.getNamespace().get("Crawl");
        System.out.println(crawl);

        crawl.setOnAction(event -> {
//            System.out.println("Hello");
            switchingScene.switchToSearching();
        });

        upload = (Button) loader.getNamespace().get("Upload");
        System.out.println(upload);

        upload.setOnAction(event -> {
            System.out.println("Hello");
            switchingScene.switchToAddFile();
        });

        staticData = (Button) loader.getNamespace().get("Static");
        staticData.setOnAction(event -> {
            switchingScene.switchToDisplay();
        });
    }

    private void init() {
        List<User> kolList = handle.getUsers();
        VBox table = controller.getTable(kolList);

        VBox vbox = (VBox) loader.getNamespace().get("Table");

        vbox.getChildren().add(table);

        scene = new Scene(root);
    }

    public void start() {
        init();
        stage.setScene(scene);
        stage.show();
    }
}
