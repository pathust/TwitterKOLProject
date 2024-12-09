package UI;

import UI.display.Display;
import UI.display.DisplayView;
import UI.home.addfile.UploadFile;
import UI.home.startscraper.Searching;
import UI.menu.MenuController;
import UI.start.StartedController;
import UI.waiting.WaitingScene;
import graph.Graph;
import graph.PagerankCalculator;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppInterface extends Application implements SwitchingScene{
    private Graph graph;
    private PagerankCalculator pagerankCalculator;
    private Searching searching;
    private UploadFile uploadFile;
    private WaitingScene waitingScene;
    private Display display;
    private DisplayView displayView;
//    private DisplayScene displayScene;
    private StartedController startedController;
    private MenuController menuController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        graph = new Graph();
        pagerankCalculator = new PagerankCalculator();

        searching = new Searching(primaryStage, this);
        waitingScene = new WaitingScene(primaryStage, this);
//        displayScene = new DisplayScene(primaryStage, this);
        menuController = new MenuController(this);
        uploadFile = new UploadFile(primaryStage, this);
        startedController = new StartedController(primaryStage, this);
        displayView = new DisplayView(primaryStage, this);
//        display = new Display(primaryStage, this);
//
//        homeScene.start();
        startedController.start();
    }

    @Override
    public void switchToSearching() {
        searching.start();
    }

    @Override
    public void switchToWaiting() {
        waitingScene.start();
    }

    @Override
    public void switchToDisplayKOL() {
        displayView.startKOL();
    }

    @Override
    public void switchToDisplayTweet() {
        displayView.startTweet();
    }

    @Override
    public void closeHome() {
        searching.close();
    }

    @Override
    public void closeWaiting() {
        waitingScene.close();
    }

    @Override
    public void closeDisplay() {
//        displayScene.close();
    }

    @Override
    public void switchToAddFile() {
        uploadFile.start();
    }
}
