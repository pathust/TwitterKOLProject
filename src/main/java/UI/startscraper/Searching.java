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
    private Button crawl, upload, staticData, addButton, searchButton;
    private ImageView background;

    private void binding() {
        menu.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.2));
        menu.prefHeightProperty().bind(anchorPane.heightProperty());
        searchField.prefWidthProperty().bind(anchorPane.widthProperty().subtract(menu.widthProperty()));
        searchField.prefHeightProperty().bind(anchorPane.heightProperty());

        background.fitWidthProperty().bind(anchorPane.widthProperty());
        background.fitHeightProperty().bind(anchorPane.heightProperty());

//        menu.prefWidthProperty().addListener((obs, oldWidth, newWidth) -> {
//            searchField.setLayoutX(menu.getWidth());
//            System.out.println(searchField.getLayoutX());
//        });

        searchField.prefWidthProperty().addListener((obs, oldWidth, newWidth) -> {
            search.setLayoutX(newWidth.doubleValue()*0.25);
            search.setPrefWidth(newWidth.doubleValue()*0.6);
            addButton.setLayoutX(search.getLayoutX());
            searchButton.setLayoutX((newWidth.doubleValue()-searchButton.getWidth())/2);
        });

        searchField.prefHeightProperty().addListener((obs, oldHeight, newHeight) -> {
            search.setLayoutY(newHeight.doubleValue()*0.35);
            search.setSpacing(newHeight.doubleValue()*0.01);
//            addButton.setLayoutX(search.getLayoutX());
            searchButton.setLayoutY(newHeight.doubleValue()*0.8);
        });

        crawl.prefWidthProperty().bind(menu.widthProperty());
        upload.prefWidthProperty().bind(menu.widthProperty());
        staticData.prefWidthProperty().bind(menu.widthProperty());
    }

    void addEventListener() {
        addButton.setOnAction(event -> searchingLogic.clickAddButton(search, addButton));
        searchButton.setOnAction(event -> searchingLogic.clickSearchButton());

        upload.setOnAction(event -> searchingLogic.clickUpload());
        staticData.setOnAction(event -> searchingLogic.clickStaticData());
    }

    public Searching() {}

    public Searching(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        loader = new FXMLLoader(getClass().getResource("/searching.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        searchingLogic = new SearchingLogic(stage, switchingScene, root);

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

        addEventListener();
        binding();

        scene = new Scene(root);
    }

    public void start() {
        stage.setScene(scene);
        this.stage.show();
    }

    public void close() {
        this.stage.close();
    }
}