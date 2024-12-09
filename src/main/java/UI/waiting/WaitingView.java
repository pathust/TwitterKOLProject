package UI.waiting;

import UI.SwitchingScene;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;

public class WaitingView {
    @FXML
    protected VBox waitingLayout;
    @FXML
    protected HBox pinkrec, hbox1,hboxButton;
    protected Stage stage;
    protected ArrayList<Rectangle> squares;
    protected static Label label = new Label();
    protected Scene waitingScene;
    protected SwitchingScene switchingScene;
    @FXML
    protected Button stopButton;

    @FXML
    private void initialize() {
        //Them ani cho hinh chu nhat
        for(int i=0; i < pinkrec.getChildren().size(); i++) {
            Rectangle square = (Rectangle) pinkrec.getChildren().get(i);

            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.millis(1000)); // Thời gian di chuyển
            transition.setNode(square); // Đặt node là hình chữ nhật
            transition.setByY(15); // Khoảng cách di chuyển theo trục Y
            transition.setCycleCount(TranslateTransition.INDEFINITE); // Lặp vô hạn
            transition.setAutoReverse(true); // Di chuyển lên và xuống
            transition.setDelay(Duration.millis(i * 200));

            transition.play();
        }

    }
    void binding() {
        pinkrec.prefWidthProperty().bind(waitingLayout.widthProperty().multiply(0.3));
        pinkrec.prefHeightProperty().bind(waitingLayout.heightProperty().multiply(0.1));

        hbox1.prefHeightProperty().bind(waitingLayout.heightProperty().multiply(0.3));
        hbox1.prefWidthProperty().bind(waitingLayout.widthProperty().multiply(0.2));
        
        hboxButton.prefWidthProperty().bind(waitingLayout.widthProperty().multiply(0.3));
        hboxButton.prefHeightProperty().bind(waitingLayout.heightProperty().multiply(0.2));
        
        stopButton.pref.bind(hboxButton.widthProperty().multiply(0.3));
        stopButton.prefHeightProperty().bind(hboxButton.heightProperty().multiply(0.2));

        label.prefHeightProperty().bind(hbox1.heightProperty().multiply(0.3));
        label.prefWidthProperty().bind(hbox1.widthProperty())
    }
}
