package UI;

import UI.display.DisplayScene;
import UI.home.HomeScene;
import UI.start.StartedController;
import UI.waiting.WaitingScene;
import graph.Graph;
import graph.PagerankCalculator;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppInterface extends Application implements SwitchingScene{
    private Graph graph;
    private PagerankCalculator pagerankCalculator;
    private HomeScene homeScene;
    private WaitingScene waitingScene;
    private DisplayScene displayScene;
    private StartedController startedController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        graph = new Graph();
        pagerankCalculator = new PagerankCalculator();

        homeScene = new HomeScene(primaryStage, this);
        waitingScene = new WaitingScene(primaryStage, this);
        displayScene = new DisplayScene(primaryStage, this);
        startedController = new StartedController(primaryStage, this);
//
//        homeScene.start();
        startedController.start();
    }

    @Override
    public void switchToHome() {
        homeScene.start();
    }

    @Override
    public void switchToWaiting() {
        waitingScene.start();
    }

    @Override
    public void switchToDisplay() {
//        displayScene.init();
        displayScene.start();
    }

    @Override
    public void closeHome() {
        homeScene.close();
    }

    @Override
    public void closeWaiting() {
        waitingScene.close();
    }

    @Override
    public void closeDisplay() {
        displayScene.close();
    }
}
