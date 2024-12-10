package UI.waiting;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.Button;
import java.util.ArrayList;

public class WaitingView {
    protected VBox waitingLayout;
    protected HBox hbox, hbox1,hboxButton;
    protected ArrayList<Rectangle> squares;
    protected static Label label = new Label();
    protected Button stopButton;

    public WaitingView() {
       waitingLayout = new VBox();
       waitingLayout.setAlignment(Pos.CENTER);
       waitingLayout.setSpacing(0);
       waitingLayout.setStyle("-fx-background-color: #FFF0F5; -fx-padding: 10;");

       hboxButton = new HBox();
       hboxButton.setAlignment(Pos.CENTER);
       stopButton = new Button("Stop Scraping");
       hboxButton.getChildren().add(stopButton);
       stopButton.setStyle("-fx-border-color: black; -fx-background-color: #FFE4E1; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;-fx-font-weight: Bold; -fx-font-size: 15;");
       stopButton.setOnMouseClicked(event ->{
           stopButton.setStyle("-fx-border-color: black; -fx-background-color: #FF99CC;-fx-border-width: 2; -fx-border-radius: 40; -fx-background-radius: 40;-fx-font-weight: Bold; -fx-font-size: 15;");
       });
       stopButton.setOnMouseReleased(event -> {
           stopButton.setStyle("-fx-border-color: black; -fx-background-color: #FFE4E1; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;-fx-font-weight: Bold; -fx-font-size: 15;");
       });
       stopButton.setOnMousePressed(event -> {
           stopButton.setStyle("-fx-border-color: black; -fx-background-color: #FFCCCC; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;-fx-font-weight: Bold; -fx-font-size: 15;");
       });

       hbox1 = new HBox();
       hbox1.setPrefSize(200,100);
       hbox1.setAlignment(Pos.CENTER);
       label.setText("Waiting for Data !");
       hbox1.getChildren().add(label);
       getLabel().setStyle("-fx-text-fill: black; -fx-font-weight: Bold; -fx-font-size: 15; -fx-alignment: center;");
       label.styleProperty().bind(
               Bindings.createStringBinding(
                       () -> String.format("-fx-font-size: %.1fpx;", hbox1.getWidth() / 25),
                       hbox1.widthProperty()))
       ;

       hbox = new HBox();
       hbox.setAlignment(Pos.CENTER);
       hbox.setSpacing(20);

       squares = new ArrayList<>();
       for(int i=0; i<4; ++i) {
           Rectangle square = new Rectangle(15, 15, Color.LIGHTPINK);

           hbox.getChildren().add(square);

           TranslateTransition transition = new TranslateTransition();
           transition.setDuration(Duration.millis(1000)); // Thời gian di chuyển
           transition.setNode(square); // Đặt node là hình chữ nhật
           transition.setByY(10); // Khoảng cách di chuyển theo trục Y
           transition.setCycleCount(TranslateTransition.INDEFINITE); // Lặp vô hạn
           transition.setAutoReverse(true); // Di chuyển lên và xuống
           transition.setDelay(Duration.millis(i * 200));

           transition.play();
        }

       waitingLayout.getChildren().addAll(hbox,hbox1,hboxButton);
    }

    void binding() {
        hbox.prefWidthProperty().bind(waitingLayout.widthProperty().multiply(0.3));
        hbox.prefHeightProperty().bind(waitingLayout.heightProperty().multiply(0.2));

        hbox1.prefWidthProperty().bind(waitingLayout.heightProperty().multiply(0.3));
        hbox1.prefHeightProperty().bind(waitingLayout.widthProperty().multiply(0.2));
        
        hboxButton.prefWidthProperty().bind(waitingLayout.widthProperty().multiply(0.3));
        hboxButton.prefHeightProperty().bind(waitingLayout.heightProperty().multiply(0.2));
        
        stopButton.prefWidthProperty().bind(hboxButton.widthProperty().multiply(0.3));
        stopButton.prefHeightProperty().bind(hboxButton.heightProperty().multiply(0.5));

//        label.prefWidthProperty().bind(labelContainer.widthProperty().multiply(0.5));
//        label.prefHeightProperty().bind(labelContainer.heightProperty().multiply(0.5));
    }

    public VBox getWaitingLayout() {
        return waitingLayout;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Label getLabel(){
        return label;
    }
}
