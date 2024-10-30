package UI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;

public class WaitingScene {
    private VBox waitingLayout;
    private HBox hbox, hbox2;
    private Stage stage;
    ArrayList<Rectangle> squares;
    Label label;
    Scene waitingScene;

    public WaitingScene(Stage stage) {
        this.stage = stage;

        this.waitingLayout = new VBox();
        this.waitingLayout.setAlignment(Pos.CENTER);

        this.hbox = new HBox();
        this.hbox.setAlignment(Pos.CENTER);
        this.hbox.setSpacing(20);
        this.waitingLayout.getChildren().add(this.hbox);

        this.squares = new ArrayList<>();
        for(int i=0; i<3; ++i) {
            Rectangle square = new Rectangle(10, 10, Color.GREY);

            this.hbox.getChildren().add(square);

            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.millis(1000)); // Thời gian di chuyển
            transition.setNode(square); // Đặt node là hình chữ nhật
            transition.setByY(10); // Khoảng cách di chuyển theo trục Y
            transition.setCycleCount(TranslateTransition.INDEFINITE); // Lặp vô hạn
            transition.setAutoReverse(true); // Di chuyển lên và xuống
            transition.setDelay(Duration.millis(i * 200));

            transition.play();
        }

        this.hbox2 = new HBox();
        this.label = new Label("Waiting for Data !");
        //label.setPrefSize(200, 20);
        this.hbox2.getChildren().add(label);
        this.hbox2.setStyle("-fx-alignment: center; -fx-padding: 20;");

        this.waitingLayout.getChildren().add(hbox2);

        this.waitingScene = new Scene(waitingLayout, 300, 300);
    }

    public void start() {
        this.stage.setScene(waitingScene);
        this.stage.show();
    }

    public void close() {
        stage.close();
    }
}
