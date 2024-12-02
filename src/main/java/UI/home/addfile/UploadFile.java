package UI.home.addfile;

import UI.SwitchingScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class UploadFile{
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    //    private MenuController menuController;
    private FXMLLoader loader;
    private AddFileHandler addFileHandler;
    private Button chooseFile, kol, tweet;
    private Button crawl, upload, staticData;
    private TextField textField;

    public UploadFile(){}

    public UploadFile(Stage primaryStage, SwitchingScene switching) {
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

        textField = (TextField) loader.getNamespace().get("TextField");
        chooseFile = (Button) loader.getNamespace().get("ChooseFile");
        kol = (Button) loader.getNamespace().get("KOL");
        tweet = (Button) loader.getNamespace().get("Tweet");

        textField.setText("No json file seclected");

        chooseFile.setOnAction(event -> {
            File selectedFile = addFileHandler.openFileDialog();

            if(selectedFile != null) {
                textField.setText(selectedFile.getAbsolutePath());
            }
        });

        kol.setOnAction(event -> {
            addFileHandler.copyFile("\\KOLs.json");
        });

        tweet.setOnAction(event -> {
            addFileHandler.copyFile("\\Tweet.json");
        });

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

        scene = new Scene(root);
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}
