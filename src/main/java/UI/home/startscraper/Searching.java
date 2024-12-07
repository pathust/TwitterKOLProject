package UI.home.startscraper;

import UI.SwitchingScene;

import UI.menu.MenuController;
import javafx.fxml.FXML;
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
    @FXML
    private Button addButton;
    @FXML
    private Button searchButton;
    private int counting = 0;
    private ArrayList<HBox> array;
    private ArrayList<TextField> arrayText;
    private FXMLLoader loader;
    private MenuController menuController;
    private VBox vbox;
    private static Button crawl, upload, staticData;

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

            vbox.getChildren().remove(xButton.getParent());
        });

        hBox.getChildren().add(textField);
        hBox.getChildren().add(xButton);

        ++counting;
        hBox.setId("HBox"+counting);
        System.out.println(textField.getId());

        vbox.getChildren().remove(addButton);
        vbox.getChildren().add(hBox);
        vbox.getChildren().add(addButton);
        array.add(hBox);
        arrayText.add(textField);
    }

    public Searching() {

    }
    public Searching(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;
//        menuController = new MenuController(switching);

        loader = new FXMLLoader(getClass().getResource("/main/resources/searching.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        System.out.println(loader.getNamespace().get("Menu"));

        vbox = (VBox)loader.getNamespace().get("VBox");
        array = new ArrayList<>();
        arrayText = new ArrayList<>();

        addButton = (Button) loader.getNamespace().get("AddButton");
        addButton.setOnAction(event -> {
            createMoreTextField();
        });

        // Khi ấn sẽ bắt đầu crawl data
        searchButton = (Button) loader.getNamespace().get("searchButton");
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
        this.stage.show();
    }

    public void close() {
        this.stage.close();
    }
}
