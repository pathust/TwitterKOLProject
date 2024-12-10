package UI.display;

import UI.SwitchingScene;
import UI.table.controller.KOLTableController;
import UI.table.controller.TableController;
import UI.table.controller.TweetTableController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Tweet;
import model.User;
import storage.DataRepository;
import storage.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.List;

// Class để quản lý giao diện và hiển thị bảng
public class Display {
    private static final Object T = null;
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    private FXMLLoader loader;
    private DataRepository dataRepository;
    private TableController kolController;
    private TableController tweetController;
    private Parent root;
    private VBox tableZone;

    public void clickChoiceBox(ChoiceBox<String> choiceBox) {
        String selected = choiceBox.getValue();

        if(selected.equals("KOL Table")) {
            switchingScene.switchToDisplayKOL();
        }
        else if(selected.equals("Tweet Table")) {
            switchingScene.switchToDisplayTweet();
        }
    }

    public void clickCrawl() {
        switchingScene.switchToSearching();
    }

    public void clickUpload() {
        switchingScene.switchToAddFile();
    }

    public Display() {}

    public Display(Stage primaryStage, SwitchingScene switching, Parent rootPane, VBox zone) {
        stage = primaryStage;
        switchingScene = switching;
        root = rootPane;
        tableZone = zone;
//        loader = new FXMLLoader(getClass().getResource("/display.fxml"));
//        root = null;
//        try {
//            root = loader.load();
//        } catch (IOException e) {
//            System.out.println("No FIle found");
//            throw new RuntimeException(e);
//        }

//        anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");
//        vBox = (VBox) loader.getNamespace().get("Table");
//        menu = (VBox) loader.getNamespace().get("menu");
//        background = (ImageView) loader.getNamespace().get("Background");
//        crawl = (Button) loader.getNamespace().get("Crawl");
//        upload = (Button) loader.getNamespace().get("Upload");
//        staticData = (Button) loader.getNamespace().get("Static");
//        choiceBox =(ChoiceBox<String>) loader.getNamespace().get("ChoiceBox");

        dataRepository = new StorageHandler();
        kolController = new KOLTableController();
        tweetController = new TweetTableController();

//        binding();

//        choiceBox.getItems().addAll("KOL Table","Tweet Table");
//        choiceBox.setValue("KOL Table");

//        choiceBox.setOnAction(event -> {
//            String selected = choiceBox.getValue();
//
//            if(selected.equals("KOL Table")) {
//                switchingScene.switchToDisplayKOL();
//            }
//            else if(selected.equals("Tweet Table")) {
//                switchingScene.switchToDisplayTweet();
//            }
//        });

//        crawl.setOnAction(event -> {
//            switchingScene.switchToSearching();
//        });
//
//        upload.setOnAction(event -> {
//            switchingScene.switchToAddFile();
//        });
//
//        staticData.setOnAction(event -> {
//            switchingScene.switchToDisplayKOL();
//        });

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

    private void addTableToUI(int signal) {
        VBox table = null;

        if(signal == 0) {
            table = kolController.getTable(readData(User.class, signal));
        } else if (signal == 1) {
            table = tweetController.getTable(readData(Tweet.class, signal));
        }

//
        tableZone.getChildren().clear();
//
        tableZone.getChildren().add(table);

        scene.setRoot(root);
    }

    public void startKOL() {
        addTableToUI(0);
        stage.setScene(scene);
        stage.show();
    }

    public void startTweet() {
        addTableToUI(1);
        stage.setScene(scene);
        stage.show();
    }
}
