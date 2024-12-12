package ui.display;

import ui.SwitchingScene;
import ui.table.controller.KOLTableController;
import ui.table.controller.TweetTableController;
import graph.Graph;
import graph.GraphFactory;
import graph.PagerankCalculator;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DataModel;
import storage.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static utils.ObjectType.TWEET;
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
    private <T extends DataModel> List<T> readData(ObjectType type, int signal) {
        String filePath = (signal == 1) ? "Tweet" : "KOLs";
        try {
            storageHandler.load(USER, "KOLs");
            storageHandler.load(TWEET, "Tweet");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<DataModel> userList = storageHandler.getAll(USER, "KOLs")
                .stream()
                .filter(Objects::nonNull)
                .map(item -> (DataModel) item)
                .toList();
        List<DataModel> tweetList = storageHandler.getAll(TWEET, "Tweet")
                .stream()
                .filter(Objects::nonNull)
                .map(item -> (DataModel) item)
                .toList();

        Graph graph = GraphFactory.createGraph(userList, tweetList);
        PagerankCalculator.calculatePageRank(graph, 100);

        List<T> list = ((type == USER) ? userList : tweetList)
                .stream()
                .filter(Objects::nonNull)
                .map(item -> (T) item)
                .toList();

        return list;
    }

    private List<DataModel> mergeFile(List<? extends DataModel> ...lists) {
        List<DataModel> list = new ArrayList<>();

        for(List<? extends DataModel> list1 : lists) {
            for(DataModel dataModel : list1) {
                list.add(dataModel);
            }
        }

        return list;
    }

    private <T extends Pane> void addTableToUI(T tableZone, int signal) {
        VBox table = null;

        if(signal == 0) {
            table = kolController.getTable(readData(USER, signal));
        } else if (signal == 1) {
            table = tweetController.getTable(readData(TWEET, signal));
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