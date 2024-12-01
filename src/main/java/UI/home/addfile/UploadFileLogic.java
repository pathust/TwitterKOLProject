package UI.home.addfile;

import UI.SwitchingScene;
import UI.menu.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UploadFileLogic {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
//    private MenuController menuController;
    private FXMLLoader loader;
    private AddFileHandler addFileHandler;
    private Button chooseFile, kol, tweet;
    private Button crawl, upload, staticData;

    public UploadFileLogic(){}

    public UploadFileLogic(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

//        menuController = new MenuController(switching);

        loader = new FXMLLoader(getClass().getResource("/uploadFile.fxml"));
//        if(loader == null) System.out.println("No File Found");
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        addFileHandler = new AddFileHandler(stage);

        chooseFile = (Button) loader.getNamespace().get("ChooseFile");
        kol = (Button) loader.getNamespace().get("KOL");
        tweet = (Button) loader.getNamespace().get("Tweet");

        chooseFile.setOnAction(event -> {
            addFileHandler.openFileDialog();
        });

        kol.setOnAction(event -> {
            addFileHandler.copyFile("KOLs.json");
        });

        tweet.setOnAction(event -> {
            addFileHandler.copyFile("Tweets.json");
        });

        crawl = (Button) loader.getNamespace().get("Crawl");
        System.out.println(crawl);

        crawl.setOnAction(event -> {
//            System.out.println("Hello");
            switchingScene.switchToHome();
        });

        upload = (Button) loader.getNamespace().get("Upload");
        System.out.println(upload);

        upload.setOnAction(event -> {
            System.out.println("Hello");
            switchingScene.switchToAddFile();
        });

        scene = new Scene(root);
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}
