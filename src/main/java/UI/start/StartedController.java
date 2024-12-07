package UI.start;

import UI.SwitchingScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartedController {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    @FXML
    private Button getStartedButton;

    @FXML
    private void initialize(){
        getStartedButton.setOnMousePressed(e -> getStartedButton.setStyle("-fx-background-color: #104e8b; -fx-text-fill: white; -fx-border-radius: 30; -fx-background-radius: 60;"));
        getStartedButton.setOnMouseMoved(e -> getStartedButton.setStyle("-fx-background-color: #1c86ee; -fx-text-fill: white;-fx-border-radius: 30; -fx-background-radius: 60;"));
        getStartedButton.setOnMouseExited(e -> getStartedButton.setStyle("-fx-background-color: linear-gradient(to bottom, #42a5f5, #1e88e5); -fx-text-fill: white;-fx-border-radius: 30; -fx-background-radius: 60;"));
    }

    public StartedController() {

    }

    public StartedController(Stage primaryStage, SwitchingScene switching) throws IOException {
        stage = primaryStage;
        switchingScene = switching;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/started.fxml"));
//        if(loader == null) System.out.println("No File Found");
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        getStartedButton = (Button) loader.getNamespace().get("getStartedButton");

        getStartedButton.setOnAction(event -> {
            switchingScene.switchToSearching();
        });

        scene = new Scene(root);
        primaryStage.initStyle(StageStyle.DECORATED);
//        primaryStage.setScene(scene);
        primaryStage.setTitle("Twitter KOL Project");
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}
