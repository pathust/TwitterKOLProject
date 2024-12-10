package UI.display;

import UI.SwitchingScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DisplayView {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Display display;

    @FXML
    private Button crawl, upload, staticData;
    @FXML
    private VBox table, menu;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Parent root;
    @FXML
    private ImageView background;
    @FXML
    private ChoiceBox<String> choiceBox;

    public DisplayView() {}

    void binding() {
        choiceBox.getItems().addAll("KOL Table","Tweet Table");
        choiceBox.setValue("KOL Table");

        choiceBox.setOnAction(event -> display.clickChoiceBox(choiceBox));

        crawl.setOnAction(event -> display.clickCrawl());
        upload.setOnAction(event -> display.clickUpload());
    }

    public DisplayView(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/display.fxml"));
        root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");
        table = (VBox) loader.getNamespace().get("table");
        menu = (VBox) loader.getNamespace().get("menu");
        background = (ImageView) loader.getNamespace().get("background");
        crawl = (Button) loader.getNamespace().get("crawl");
        upload = (Button) loader.getNamespace().get("upload");
        staticData = (Button) loader.getNamespace().get("staticData");
        choiceBox =(ChoiceBox<String>) loader.getNamespace().get("choiceBox");

        display = new Display(stage, switchingScene, root);

        binding();
    }

    public void startKOL() {
        display.startKOL(table);
    }

    public void startTweet() {
        display.startTweet(table);
    }
}