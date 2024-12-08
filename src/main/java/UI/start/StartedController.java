package UI.start;

import UI.SwitchingScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartedController {
    @FXML
    private ImageView kolsearch1;
    @FXML
    private ImageView kolsearch2;
    @FXML
    private AnchorPane anchorPane;
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    @FXML
    private Button getStartedButton;

//    @FXML
//    private void initialize(){
//        getStartedButton.setOnMousePressed(e -> getStartedButton.setStyle("-fx-background-color: #104e8b; -fx-text-fill: white; -fx-border-radius: 30; -fx-background-radius: 60;"));
//        getStartedButton.setOnMouseMoved(e -> getStartedButton.setStyle("-fx-background-color: #1c86ee; -fx-text-fill: white;-fx-border-radius: 30; -fx-background-radius: 60;"));
//        getStartedButton.setOnMouseExited(e -> getStartedButton.setStyle("-fx-background-color: linear-gradient(to bottom, #42a5f5, #1e88e5); -fx-text-fill: white;-fx-border-radius: 30; -fx-background-radius: 60;"));
//    }

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

        kolsearch1 = (ImageView) loader.getNamespace().get("kolsearch1");
        kolsearch2 = (ImageView) loader.getNamespace().get("kolsearch2");
        anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");

//        kolsearch1.layoutXProperty().bind(anchorPane.heightProperty().multiply(0.2));
        kolsearch1.fitWidthProperty().bind(anchorPane.widthProperty().multiply(1.3));
        kolsearch1.fitHeightProperty().bind(anchorPane.heightProperty());

        kolsearch2.fitWidthProperty().bind(anchorPane.widthProperty().multiply(1.3));
        kolsearch2.fitHeightProperty().bind(anchorPane.heightProperty());

        getStartedButton = (Button) loader.getNamespace().get("getStartedButton");

        getStartedButton.setOnAction(event -> {
            switchingScene.switchToAddFile();
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
