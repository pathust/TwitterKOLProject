package UI.waiting;

import UI.SwitchingScene;
import UI.startscraper.StartScraperHandler;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WaitingScene {
    private Stage stage;
    private Scene waitingScene;
    private SwitchingScene switchingScene;
    private static WaitingView waitingView;

    public WaitingScene(Stage stage, SwitchingScene switching) {
        this.stage = stage;
        switchingScene = switching;

        waitingView = new WaitingView();
        Parent root = waitingView.getWaitingLayout();

        waitingScene = new Scene(root, 900,600);

        waitingView.getStopButton().setOnAction(event ->{
            Platform.runLater(() -> {
//                stage.close();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StartScraperHandler.closeThread();
            });
            switchingScene.switchToSearching();
        });
//        waitingView.binding();
        stage.setOnCloseRequest(this::handleCloseRequest);
    }

    public static void updateStatus(String state) {
        Platform.runLater(() -> {
            WaitingScene.waitingView.getLabel().setText(state);
        });
    }

    public void start() {
        this.stage.setScene(waitingScene);
        this.stage.show();
    }

    public void close() {
        stage.close();
    }

    private void handleCloseRequest(WindowEvent event) {
        Platform.runLater(() -> {
            this.close();
            StartScraperHandler.closeThread();
            System.exit(0);
        });
    }
}
