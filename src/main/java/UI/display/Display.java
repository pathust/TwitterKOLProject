package UI.display;

import UI.SwitchingScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Tweet;
import model.User;
import storage.DataRepository;
import storage.StorageHandler;
import utils.ObjectType;

import java.awt.*;
import java.io.IOException;
import java.util.List;

// Class để quản lý giao diện và hiển thị bảng
public class Display {
    private static final Object T = null;
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    private FXMLLoader loader;
    private Button crawl, upload, staticData;
    private VBox vBox, menu;
    private AnchorPane anchorPane;
    private DataRepository dataRepository;
    private TableController kolController;
    private TableController tweetController;
    private Parent root;
    private ImageView background;
    private ChoiceBox<String> choiceBox;

    private void binding() {
        menu.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.2));
        menu.prefHeightProperty().bind(anchorPane.heightProperty());

        background.fitWidthProperty().bind(anchorPane.widthProperty());
        background.fitHeightProperty().bind(anchorPane.heightProperty());

        crawl.prefWidthProperty().bind(menu.widthProperty());
        upload.prefWidthProperty().bind(menu.widthProperty());
        staticData.prefWidthProperty().bind(menu.widthProperty());

        vBox.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.7));
        vBox.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.5));

        choiceBox.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.3));
//        choiceBox.prefHeightProperty().bind(anchorPane.heightProperty().multiply());

        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            vBox.setLayoutX(newWidth.doubleValue() * 0.25);
//            vBox.setPrefWidth(newWidth.doubleValue() * 0.7);

            choiceBox.setLayoutX(newWidth.doubleValue() * 0.25);
//            choiceBox.setPrefWidth(newWidth.doubleValue() * 0.3);
        });

        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            vBox.setLayoutY(newHeight.doubleValue() * 0.4);

            choiceBox.setLayoutY(newHeight.doubleValue() * 0.3);
        });
    }

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

        anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");
        vBox = (VBox) loader.getNamespace().get("Table");
        menu = (VBox) loader.getNamespace().get("menu");
        background = (ImageView) loader.getNamespace().get("Background");
        crawl = (Button) loader.getNamespace().get("Crawl");
        upload = (Button) loader.getNamespace().get("Upload");
        staticData = (Button) loader.getNamespace().get("Static");
        choiceBox =(ChoiceBox<String>) loader.getNamespace().get("ChoiceBox");

        dataRepository = new StorageHandler();
        kolController = new KOLTableController();
        tweetController = new TweetTableController();

        binding();

        choiceBox.getItems().addAll("KOL Table","Tweet Table");
        choiceBox.setValue("KOL Table");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {

        });

        choiceBox.setOnAction(event -> {
            String selected = choiceBox.getValue();

            if(selected.equals("KOL Table")) {
                switchingScene.switchToDisplayKOL();
            }
            else if(selected.equals("Tweet Table")) {
                switchingScene.switchToDisplayTweet();
            }
        });

        crawl.setOnAction(event -> {
            switchingScene.switchToSearching();
        });

        upload.setOnAction(event -> {
            switchingScene.switchToAddFile();
        });

        staticData.setOnAction(event -> {
            switchingScene.switchToDisplayKOL();
        });

        scene = new Scene(root);
    }

    // signal = 0: KOL, = 1: Tweet
    private <T> List<T> readData(Class<T> objType, int signal) {
        String filePath = null;
        ObjectType type = null;

        if(signal == 0) {
            filePath = "KOLs.json";
            type = ObjectType.valueOf("USER");
        }
        else if(signal == 1) {
            filePath = "Tweet.json";
            type = ObjectType.valueOf("TWEET");
        }

        try {
            dataRepository.load(type, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<T> list = null;
        try {
            list = dataRepository.getAll(type, filePath)
                    .stream()
                    .filter(item -> objType.isInstance(item))
                    .map(item -> (T) item)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private void init(int signal) {
        VBox table = null;

        if(signal == 0) {
            table = kolController.getTable(readData(User.class, signal));
        } else if (signal == 1) {
            table = tweetController.getTable(readData(Tweet.class, signal));
        }

//
        vBox.getChildren().clear();
//
        vBox.getChildren().add(table);

        scene.setRoot(root);
    }

    public void startKOL() {
        init(0);
        stage.setScene(scene);
        stage.show();
    }

    public void startTweet() {
        init(1);
        stage.setScene(scene);
        stage.show();
    }
}