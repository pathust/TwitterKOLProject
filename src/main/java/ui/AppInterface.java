package ui;

import ui.display.DisplayView;
import ui.addfile.UploadFile;
import ui.startscraper.Searching;
import ui.start.StartedController;
import ui.waiting.WaitingScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppInterface extends Application implements SwitchingScene{
    private Searching searching;
    private UploadFile uploadFile;
    private WaitingScene waitingScene;
    private DisplayView displayView;
    private StartedController startedController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        searching = new Searching(primaryStage, this);
        waitingScene = new WaitingScene(primaryStage, this);
        uploadFile = new UploadFile(primaryStage, this);
        startedController = new StartedController(primaryStage, this);
        displayView = new DisplayView(primaryStage, this);
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
