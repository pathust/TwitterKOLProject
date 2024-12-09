package UI.waiting;

import UI.SwitchingScene;
import UI.home.startscraper.StartScraperHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.util.ArrayList;

public class WaitingScene {
    private VBox waitingLayout;
    private HBox hbox, hbox2;
    private Stage stage;
    private ArrayList<Rectangle> squares;
    @FXML
    private static Label label = new Label();
    private Scene waitingScene;
    private SwitchingScene switchingScene;

    public WaitingScene(Stage stage, SwitchingScene switching) {
        this.stage = stage;
        switchingScene = switching;

        waitingLayout = new VBox();
        waitingLayout.setAlignment(Pos.CENTER);
        waitingLayout.setStyle("-fx-background-color: #FFF0F5;");

        hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(30);
        waitingLayout.getChildren().add(this.hbox);

        squares = new ArrayList<>();
        for(int i=0; i<3; ++i) {
            Rectangle square = new Rectangle(15, 15, Color.LIGHTPINK);

            hbox.getChildren().add(square);

            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.millis(1000)); // Thời gian di chuyển
            transition.setNode(square); // Đặt node là hình chữ nhật
            transition.setByY(15); // Khoảng cách di chuyển theo trục Y
            transition.setCycleCount(TranslateTransition.INDEFINITE); // Lặp vô hạn
            transition.setAutoReverse(true); // Di chuyển lên và xuống
            transition.setDelay(Duration.millis(i * 200));

            transition.play();
        }

        hbox2 = new HBox();
        label.setText("Waiting for Data !");
        label.setStyle("-fx-font-family: Verdana; -fx-font-size: 20px;-fx-font-weight: Bold");
        //label.setPrefSize(200, 20);
        hbox2.getChildren().add(label);

        waitingLayout.getChildren().add(hbox2);

        waitingScene = new Scene(waitingLayout, 600, 600);

        stage.setOnCloseRequest(this::handleCloseRequest);
    }

    public static void updateStatus(String state) {
        Platform.runLater(() -> {
            WaitingScene.label.setText(state);
            WaitingScene.label.setStyle("-fx-font-family: Verdana; -fx-font-size: 20px;-fx-font-weight: Bold ");;
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
//            System.out.print("Hello");
            this.close();

            StartScraperHandler.closeThread();
            System.exit(0);
        });
    }
}
