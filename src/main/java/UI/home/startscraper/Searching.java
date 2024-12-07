package UI.home.startscraper;

import UI.SwitchingScene;

import UI.menu.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Searching {
    private Scene scene;
    private Stage stage;
    private SwitchingScene switchingScene;
//    @FXML
//    private Button addButton;
//    @FXML
//    private Button searchButton;
    private int counting = 0;
    private ArrayList<HBox> array;
    private ArrayList<TextField> arrayText;
    private FXMLLoader loader;
    private MenuController menuController;
    private VBox search;
//    private static Button crawl, upload, staticData;
//    private static AnchorPane anchorPane;
//    private static VBox menu;

    private void createMoreTextField() {
        if(array.size() == 5) return ;
        HBox hBox = new HBox();
        hBox.getStyleClass().add("hBoxStyle");

        TextField textField = new TextField();
        textField.getStyleClass().add("textFieldStyle");

        Button xButton = new Button();
        xButton.getStyleClass().add("xButtonStyle");

        xButton.setOnAction(event -> {
            for(int i=0; i<array.size(); ++i) {
                if(array.get(i).getId().equals(xButton.getParent().getId())) {
                    array.remove(i);
                    arrayText.remove(i);
                    break;
                }
            }

            search.getChildren().remove(xButton.getParent());
        });

        hBox.getChildren().add(textField);
        hBox.getChildren().add(xButton);

        ++counting;
        hBox.setId("HBox"+counting);

        Button addButton = (Button) loader.getNamespace().get("AddButton");
        search.getChildren().remove(addButton);
        search.getChildren().add(hBox);
        search.getChildren().add(addButton);
        array.add(hBox);
        arrayText.add(textField);
    }

    public Searching() {

    }
    public Searching(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;
//        menuController = new MenuController(switching);

        loader = new FXMLLoader(getClass().getResource("/searching.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        AnchorPane anchorPane = (AnchorPane) loader.getNamespace().get("AnchorPane");

        VBox menu = (VBox) loader.getNamespace().get("Menu");
        menu.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.2));
        menu.prefHeightProperty().bind(anchorPane.heightProperty().multiply(1));

        search = (VBox)loader.getNamespace().get("Search");
        array = new ArrayList<>();
        arrayText = new ArrayList<>();

        Button addButton = (Button) loader.getNamespace().get("AddButton");
        addButton.setOnAction(event -> {
            createMoreTextField();
        });

        // Khi ấn sẽ bắt đầu crawl data
        Button searchButton = (Button) loader.getNamespace().get("searchButton");
        searchButton.setOnAction(event -> {
            if(array.isEmpty()) return ;
            StringBuilder text = new StringBuilder();

            for (TextField tf : arrayText) {
                System.out.println(tf.getText());
                text.append(tf.getText());
                text.append("\n");
            }

            StartScraperHandler startScraperHandler = new StartScraperHandler(switchingScene);
            startScraperHandler.startCrawl(text.toString());
        });

        Button crawl = (Button) loader.getNamespace().get("Crawl");
        crawl.prefWidthProperty().bind(menu.widthProperty());
        crawl.setOnAction(event -> {
            switchingScene.switchToSearching();
        });

        Button upload = (Button) loader.getNamespace().get("Upload");
        upload.prefWidthProperty().bind(menu.widthProperty());
        upload.setOnAction(event -> {
            switchingScene.switchToAddFile();
        });

        Button staticData = (Button) loader.getNamespace().get("Static");
        staticData.prefWidthProperty().bind(menu.widthProperty());
        staticData.setOnAction(event -> {
            switchingScene.switchToDisplay();
        });

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
