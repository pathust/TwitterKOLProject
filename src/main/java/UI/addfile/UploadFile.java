package UI.addfile;

import UI.SwitchingScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UploadFile{
    private Scene scene;
    private UploadFileLogic uploadFileLogic;

    private FXMLLoader loader;
    private Button chooseFile, kol, tweet;
    private Button crawl, upload, staticData;
    private TextField textField;
    @FXML
    private VBox vBox, menu;
    private HBox hBox1, hBox2;
    private AnchorPane anchorPane;
    private ImageView background;

    void extractElement() {
        loader = new FXMLLoader(getClass().getResource("/uploadFile.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        background = (ImageView) loader.getNamespace().get("Background");
        textField = (TextField) loader.getNamespace().get("TextField");
        chooseFile = (Button) loader.getNamespace().get("ChooseFile");
        kol = (Button) loader.getNamespace().get("KOL");
        tweet = (Button) loader.getNamespace().get("Tweet");
        crawl = (Button) loader.getNamespace().get("Crawl");
        upload = (Button) loader.getNamespace().get("Upload");
        staticData = (Button) loader.getNamespace().get("Static");
        vBox = (VBox) loader.getNamespace().get("VBox");
        hBox1 = (HBox) loader.getNamespace().get("HBox1");
        hBox2 = (HBox) loader.getNamespace().get("HBox2");
        menu = (VBox) loader.getNamespace().get("menu");
        anchorPane = (AnchorPane) loader.getNamespace().get("AnchorPane");

        scene = new Scene(root);
    }

    private void addEventHandler() {
        textField.setText("No json file selected");

        chooseFile.setOnAction(event -> {
            uploadFileLogic.clickChooseFile(textField);
        });

        kol.setOnAction(event -> {
            uploadFileLogic.clickAddKOL();
        });

        tweet.setOnAction(event -> {
            uploadFileLogic.clickAddTweet();
        });

        crawl.setOnAction(event -> {
            uploadFileLogic.clickCrawl();
        });

        staticData.setOnAction(event -> {
            uploadFileLogic.clickStatic();
        });
    }

    public UploadFile(){}

    public UploadFile(Stage primaryStage, SwitchingScene switching) {
        uploadFileLogic = new UploadFileLogic(primaryStage, switching);

        extractElement();
        addEventHandler();
    }

    public void start() {
        uploadFileLogic.start(scene);
    }
}