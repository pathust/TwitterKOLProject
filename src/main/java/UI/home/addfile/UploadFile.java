package UI.home.addfile;

import UI.SwitchingScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UploadFile{
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene scene;
    private UploadFileLogic uploadFileLogic;

    private FXMLLoader loader;
    private Button chooseFile, kol, tweet;
    private Button crawl, upload, staticData;
    private TextField textField;
    private VBox vBox, menu;
    private HBox hBox1, hBox2;
    private AnchorPane anchorPane;
    private ImageView background;

    void binding() {
        menu.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.2));
        menu.prefHeightProperty().bind(anchorPane.heightProperty());

        background.fitWidthProperty().bind(anchorPane.widthProperty());
        background.fitHeightProperty().bind(anchorPane.heightProperty());

        crawl.prefWidthProperty().bind(menu.widthProperty());
        upload.prefWidthProperty().bind(menu.widthProperty());
        staticData.prefWidthProperty().bind(menu.widthProperty());

        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            vBox.setLayoutX(newWidth.doubleValue() * 0.3);
            vBox.setPrefWidth(newWidth.doubleValue() * 0.5);
            System.out.println("1");
        });

        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            vBox.setLayoutY(newHeight.doubleValue() * 0.5);
            vBox.setSpacing(newHeight.doubleValue() * 0.1);
        });

        hBox1.prefHeightProperty().bind(vBox.widthProperty());
        hBox1.prefHeightProperty().bind(vBox.heightProperty().multiply(0.45));
        hBox1.spacingProperty().bind(vBox.widthProperty().multiply(0.01));

        textField.prefWidthProperty().bind(hBox1.widthProperty().multiply(0.7));
        textField.prefHeightProperty().bind(hBox1.heightProperty());

        chooseFile.setWrapText(true);
        chooseFile.prefWidthProperty().bind(hBox1.widthProperty().multiply(0.29));
        chooseFile.prefHeightProperty().bind(hBox1.heightProperty());

        hBox2.prefHeightProperty().bind(vBox.widthProperty());
        hBox2.prefHeightProperty().bind(vBox.heightProperty().multiply(0.45));
        hBox2.spacingProperty().bind(vBox.widthProperty().multiply(0.2));

        kol.setWrapText(true);
        kol.prefWidthProperty().bind(hBox2.widthProperty().multiply(0.3));
        kol.prefHeightProperty().bind(hBox2.heightProperty());

        tweet.setWrapText(true);
        tweet.prefWidthProperty().bind(hBox2.widthProperty().multiply(0.3));
        tweet.prefHeightProperty().bind(hBox2.heightProperty());
    }

    public UploadFile(){}

    public UploadFile(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        loader = new FXMLLoader(getClass().getResource("/uploadFile.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        background = (ImageView) loader.getNamespace().get("Background");
        textField = (TextField) loader.getNamespace().get("TextField");
        chooseFile = (Button) loader.getNamespace().get("ChooseFile");
        kol = (Button) loader.getNamespace().get("KOL");
        tweet = (Button) loader.getNamespace().get("Tweet");
        crawl = (Button) loader.getNamespace().get("Crawl");
        upload = (Button) loader.getNamespace().get("Upload");
        staticData = (Button) loader.getNamespace().get("Static");
        vBox = (VBox) loader.getNamespace().get("VBox");
        hBox1 = (HBox) loader.getNamespace().get("HBox1");
        hBox2 = (HBox) loader.getNamespace().get("HBox2");
        menu = (VBox) loader.getNamespace().get("menu");
        anchorPane = (AnchorPane) loader.getNamespace().get("AnchorPane");

        textField.setText("No json file selected");
        binding();

        uploadFileLogic = new UploadFileLogic(stage, switchingScene);

        chooseFile.setOnAction(event -> {
            uploadFileLogic.clickChooseFile(textField);
        });

        kol.setOnAction(event -> {
            uploadFileLogic.clickAddKOL();
        });

        tweet.setOnAction(event -> {
            uploadFileLogic.clickAddTweet();
        });

        crawl.setOnAction(event -> {
            uploadFileLogic.clickCrawl();
        });

        staticData.setOnAction(event -> {
            uploadFileLogic.clickStatic();
        });

        scene = new Scene(root);
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}
