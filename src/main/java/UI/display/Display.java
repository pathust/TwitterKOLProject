package UI.display;

import UI.SwitchingScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Tweet;
import model.User;
import storage.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.List;

import static utils.ObjectType.USER;

// Class để quản lý giao diện và hiển thị bảng
public class Display {
    private static final Object T = null;
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    private FXMLLoader loader;
    private TableController kolController;
    private TableController tweetController;
    private Parent root;
    private StorageHandler storageHandler;

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

    public Display(Stage primaryStage, SwitchingScene switching, Parent rootPane) {
        stage = primaryStage;
        switchingScene = switching;
        root = rootPane;

        storageHandler = new StorageHandler();
        kolController = new KOLTableController();
        tweetController = new TweetTableController();

        scene = new Scene(root);
    }

    // signal = 0: KOL, = 1: Tweet
    private <T> List<T> readData(Class<T> objType, int signal) {
        String filePath = null;
        ObjectType type = null;

        if(signal == 0) {
            filePath = "KOLs";
            type = ObjectType.valueOf("USER");
        }
        else if(signal == 1) {
            filePath = "Tweet";
            type = ObjectType.valueOf("TWEET");
        }

        try {
            storageHandler.load(type, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<T> list = null;
        try {
            list = storageHandler.getAll(type, filePath)
                    .stream()
                    .filter(item -> objType.isInstance(item))
                    .map(item -> (T) item)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private void addTableToUI(VBox tableZone, int signal) {
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

    public void startKOL(VBox tableZone) {
        addTableToUI(tableZone,0);
        stage.setScene(scene);
        stage.show();
    }

    public void startTweet(VBox tableZone) {
        addTableToUI(tableZone,1);
        stage.setScene(scene);
        stage.show();
    }
}
