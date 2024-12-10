package UI.start;

import UI.SwitchingScene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartedController {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    @FXML
    private Button getStartedButton;

    public StartedController() {}

    public StartedController(Stage primaryStage, SwitchingScene switching) throws IOException {
        stage = primaryStage;
        switchingScene = switching;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/started.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }
        AnchorPane anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");
//        ImageView backgroundImage = (ImageView) loader.getNamespace().get("background");
//
//        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
//        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());

        getStartedButton = (Button) loader.getNamespace().get("getStartedButton");

//        getStartedButton.setStyle("-fx-font-size: 14px;");
//        getStartedButton.setWrapText(true);
//        anchorPane.widthProperty().addListener((observable, oldvalue, newvalue) -> {
//                getStartedButton.setPrefWidth(anchorPane.getWidth() * 0.45);
//                getStartedButton.setPrefHeight(anchorPane.getHeight()*0.2);
//
//                getStartedButton.setLayoutX((anchorPane.getWidth() - getStartedButton.getWidth()) * 0.5);
//                getStartedButton.setLayoutY(anchorPane.getHeight()*0.7);
//            System.out.println(anchorPane.getWidth()+" "+anchorPane.getHeight());
//                System.out.println("W "+getStartedButton.getWidth()+" "+getStartedButton.getHeight()+" "+getStartedButton.getLayoutX()+" "+getStartedButton.getLayoutY());
//        });

//        anchorPane.heightProperty().addListener((observable, oldvalue, newvalue) -> {
//            getStartedButton.setPrefHeight(anchorPane.getHeight()*0.2);
//            getStartedButton.setPrefWidth(anchorPane.getWidth() * 0.45);
//            getStartedButton.setLayoutY(anchorPane.getHeight()*0.7);
//
//
//            getStartedButton.setLayoutX((anchorPane.getWidth() - getStartedButton.getWidth()) * 0.5);
//            System.out.println(anchorPane.getWidth()+" "+anchorPane.getHeight());
//            System.out.println("H "+getStartedButton.getWidth()+" "+getStartedButton.getHeight()+" "+getStartedButton.getLayoutX()+" "+getStartedButton.getLayoutY());
//        });

        getStartedButton.setOnAction(event -> {
            switchingScene.switchToSearching();
        });

        scene = new Scene(root);
        primaryStage.initStyle(StageStyle.DECORATED);

        primaryStage.setTitle("Twitter KOL Project");
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}
