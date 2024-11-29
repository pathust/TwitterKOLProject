package UI.startedScene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class started {
    @FXML
    private Button getStartedButton;

    @FXML
    private void initialize(){
        getStartedButton.setOnMousePressed(e -> getStartedButton.setStyle("-fx-background-color: #104e8b; -fx-text-fill: white; -fx-border-radius: 30; -fx-background-radius: 60;"));
        getStartedButton.setOnMouseMoved(e -> getStartedButton.setStyle("-fx-background-color: #1c86ee; -fx-text-fill: white;-fx-border-radius: 30; -fx-background-radius: 60;"));
        getStartedButton.setOnMouseExited(e -> getStartedButton.setStyle("-fx-background-color: linear-gradient(to bottom, #42a5f5, #1e88e5); -fx-text-fill: white;-fx-border-radius: 30; -fx-background-radius: 60;"));
    }
    @FXML
    private void GetStarted(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/UI/home/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}