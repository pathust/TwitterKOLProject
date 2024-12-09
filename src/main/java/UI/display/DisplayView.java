package UI.display;

import UI.SwitchingScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DisplayView {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Display display;

    @FXML
    protected Button crawl, upload, staticData;
    @FXML
    protected VBox table, menu;
    @FXML
    protected AnchorPane anchorPane;
    @FXML
    protected Parent root;
    @FXML
    protected ImageView background;
    @FXML
    protected ChoiceBox<String> choiceBox;

    public DisplayView() {}

    void binding() {
        menu.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.2));
        menu.prefHeightProperty().bind(anchorPane.heightProperty());

        background.fitWidthProperty().bind(anchorPane.widthProperty());
        background.fitHeightProperty().bind(anchorPane.heightProperty());

        crawl.prefWidthProperty().bind(menu.widthProperty());
        upload.prefWidthProperty().bind(menu.widthProperty());
        staticData.prefWidthProperty().bind(menu.widthProperty());

        table.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.7));
        table.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.5));

        choiceBox.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.3));
//        choiceBox.prefHeightProperty().bind(anchorPane.heightProperty().multiply());

        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            table.setLayoutX(newWidth.doubleValue() * 0.25);
//            vBox.setPrefWidth(newWidth.doubleValue() * 0.7);

            choiceBox.setLayoutX(newWidth.doubleValue() * 0.25);
//            choiceBox.setPrefWidth(newWidth.doubleValue() * 0.3);

            choiceBox.setOnAction(event -> display.clickChoiceBox(choiceBox));

            crawl.setOnAction(event -> display.clickCrawl());
            upload.setOnAction(event -> display.clickUpload());
        });

        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            table.setLayoutY(newHeight.doubleValue() * 0.4);

            choiceBox.setLayoutY(newHeight.doubleValue() * 0.3);
        });

        choiceBox.getItems().addAll("KOL Table","Tweet Table");
        choiceBox.setValue("KOL Table");
    }

    public DisplayView(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/display.fxml"));
        root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

                loader = new FXMLLoader(getClass().getResource("/display.fxml"));
        root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        anchorPane = (AnchorPane) loader.getNamespace().get("anchorPane");
        table = (VBox) loader.getNamespace().get("table");
        menu = (VBox) loader.getNamespace().get("menu");
        background = (ImageView) loader.getNamespace().get("background");
        crawl = (Button) loader.getNamespace().get("crawl");
        upload = (Button) loader.getNamespace().get("upload");
        staticData = (Button) loader.getNamespace().get("staticData");
        choiceBox =(ChoiceBox<String>) loader.getNamespace().get("choiceBox");

        display = new Display(stage, switchingScene, root, table);

        binding();
    }

    public void initialize() {

    }

    public void startKOL() {
        display.startKOL();
    }

    public void startTweet() {
        display.startTweet();
    }
}
