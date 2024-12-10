package UI.display;

import UI.SwitchingScene;
import UI.table.controller.KOLTableController;
import UI.table.controller.TableController;
import UI.table.controller.TweetTableController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DataModel;
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
    private KOLTableController kolController;
    private TweetTableController tweetController;
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

    private void setUpScene() {
        stage.setResizable(false);
    }

    public Display() {}

    public Display(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        storageHandler = new StorageHandler();
        kolController = new KOLTableController();
        tweetController = new TweetTableController();

        setUpScene();
    }

    // signal = 0: KOL, = 1: Tweet
    private <T extends DataModel> List<T> readData(Class<T> objType, int signal) {
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

        List<DataModel> tmp = null;
        List<T> list = null;

        try {
           tmp  = storageHandler.getAll(type, filePath)
                    .stream()
                    .filter(item -> objType.isInstance(item))
                    .map(item -> (DataModel) item)
                    .toList();

           list = tmp.stream()
                    .filter(item -> objType.isInstance(item))
                    .map(item -> (T) item)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private <T extends Pane> void addTableToUI(T tableZone, int signal) {
        VBox table = null;

        if(signal == 0) {
            table = kolController.getTable(readData(User.class, signal));
        } else if (signal == 1) {
            table = tweetController.getTable(readData(Tweet.class, signal));
        }

        tableZone.getChildren().clear();

        tableZone.getChildren().add(table);
    }

    public <T extends Pane> void startKOL(T tableZone, Scene scene) {
        addTableToUI(tableZone,0);
        stage.setScene(scene);
        stage.show();
    }

    public <T extends Pane> void startTweet(T tableZone, Scene scene) {
        addTableToUI(tableZone,1);
        stage.setScene(scene);
        stage.show();
    }
}