package UI.home.startscraper;

import UI.SwitchingScene;

import UI.menu.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Searching {
    private Scene scene;
    private Stage stage;
    private SwitchingScene switchingScene;
    private int counting = 0;

    protected ArrayList<HBox> array;
    protected ArrayList<TextField> arrayText;
    protected FXMLLoader loader;
    protected MenuController menuController;
    protected VBox search;
    protected AnchorPane anchorPane;
    protected VBox menu, searchField;
    protected Button crawl, upload, staticData, addButton, searchButton;
    protected ImageView background;

    private void createMoreTextField() {
        if(array.size() == 5) return ;
        HBox hBox = new HBox();
        hBox.prefWidthProperty().bind(search.widthProperty());
        hBox.prefHeightProperty().bind(search.heightProperty().multiply(0.15));
        hBox.spacingProperty().bind(search.widthProperty().multiply(0.1));

        TextField textField = new TextField();
        textField.prefWidthProperty().bind(hBox.widthProperty().multiply(0.8));
        textField.prefHeightProperty().bind(hBox.heightProperty());

        Button xButton = new Button("X");
        xButton.prefHeightProperty().bind(hBox.heightProperty());
        xButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.1));


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
            System.out.println(addButton.getLayoutX());
        });

        searchField.prefHeightProperty().addListener((obs, oldHeight, newHeight) -> {
            search.setLayoutY(newHeight.doubleValue()*0.35);
            search.setSpacing(newHeight.doubleValue()*0.01);
//            addButton.setLayoutX(search.getLayoutX());
            searchButton.setLayoutY(newHeight.doubleValue()*0.8);
            System.out.println(addButton.getLayoutX());
        });

        crawl.prefWidthProperty().bind(menu.widthProperty());
        upload.prefWidthProperty().bind(menu.widthProperty());
        staticData.prefWidthProperty().bind(menu.widthProperty());
    }

    void addEventListener() {
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

        addButton.setOnAction(event -> {
            createMoreTextField();
        });

        // Khi ấn sẽ bắt đầu crawl data


        crawl.setOnAction(event -> {
            switchingScene.switchToSearching();
        });

        upload.setOnAction(event -> {
            switchingScene.switchToAddFile();
        });

        staticData.setOnAction(event -> {
            switchingScene.switchToDisplayKOL();
        });
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

        array = new ArrayList<>();
        arrayText = new ArrayList<>();

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