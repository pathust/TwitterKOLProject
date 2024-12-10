package UI.startscraper;

import UI.SwitchingScene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Searching {
    private Scene scene;
    private Stage stage;
    private SwitchingScene switchingScene;
    private SearchingLogic searchingLogic;

    private FXMLLoader loader;
    private VBox search;
    @FXML
    private AnchorPane anchorPane;
    private VBox menu, searchField;
    @FXML
    private Button crawl, upload, staticData, addButton, searchButton, resume;
    private ImageView background;

    private void extractElement() {
        loader = new FXMLLoader(getClass().getResource("/search.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");
        menu = (VBox) loader.getNamespace().get("Menu");
        background = (ImageView) loader.getNamespace().get("Background");
        search = (VBox)loader.getNamespace().get("Search");
        addButton = (Button) loader.getNamespace().get("AddButton");
        searchButton = (Button) loader.getNamespace().get("searchButton");
        crawl = (Button) loader.getNamespace().get("Crawl");
        upload = (Button) loader.getNamespace().get("Upload");
        staticData = (Button) loader.getNamespace().get("Static");
        searchField = (VBox) loader.getNamespace().get("SearchField");
        resume = (Button) loader.getNamespace().get("resume");

        scene = new Scene(root);
    }

    private void addEventListener() {
        addButton.setOnAction(event -> searchingLogic.clickAddButton(search, addButton));
        searchButton.setOnAction(event -> searchingLogic.clickSearchButton());
        resume.setOnAction(event -> searchingLogic.clickResumeButton());

        upload.setOnAction(event -> searchingLogic.clickUpload());
        staticData.setOnAction(event -> searchingLogic.clickStaticData());
    }

    public Searching() {}

    public Searching(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        loader = new FXMLLoader(getClass().getResource("/main/resources/search.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");
        menu = (VBox) loader.getNamespace().get("Menu");
        search = (VBox)loader.getNamespace().get("Search");
        addButton = (Button) loader.getNamespace().get("AddButton");
        searchButton = (Button) loader.getNamespace().get("searchButton");
        crawl = (Button) loader.getNamespace().get("Crawl");
        upload = (Button) loader.getNamespace().get("Upload");
        staticData = (Button) loader.getNamespace().get("Static");
        searchField = (VBox) loader.getNamespace().get("SearchField");
        resume = (Button) loader.getNamespace().get("resume");

        addEventListener();

        scene = new Scene(root);

        searchingLogic = new SearchingLogic(stage, switchingScene);

        extractElement();

        addEventListener();
    }

    public void start() {
        searchingLogic.start(scene);
    }

    public void close() {
        searchingLogic.close();
    }
}