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

    public StartedController() {

    }
    public StartedController(Stage primaryStage, SwitchingScene switching) throws IOException {
        stage = primaryStage;
        switchingScene = switching;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/started.fxml"));
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
