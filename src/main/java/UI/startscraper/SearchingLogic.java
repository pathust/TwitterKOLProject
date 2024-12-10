package UI.startscraper;

import UI.SwitchingScene;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SearchingLogic {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Parent root;

    StartScraperHandler startScraperHandler;
    private int counting = 0;
    protected ArrayList<HBox> array;
    protected ArrayList<TextField> arrayText;

    // Tạo một TextField rồi thêm vào search, đẩy vị trí addButton xuống dưới.
    public void createMoreTextField(VBox search, Button addButton) {
        if(array.size() == 5) return ;
        HBox hBox = new HBox();
        hBox.prefWidthProperty().bind(search.widthProperty());
        hBox.prefHeightProperty().bind(search.heightProperty().multiply(0.15));
        hBox.spacingProperty().bind(search.widthProperty().multiply(0.1));

        TextField textField = new TextField();
        textField.prefWidthProperty().bind(hBox.widthProperty().multiply(0.8));
        textField.prefHeightProperty().bind(hBox.heightProperty());

        Button xButton = new Button("X");
        xButton.prefHeightProperty().bind(hBox.heightProperty());
        xButton.prefWidthProperty().bind(hBox.widthProperty().multiply(0.1));


        xButton.setOnAction(event -> {
            for(int i=0; i<array.size(); ++i) {
                if(array.get(i).getId().equals(xButton.getParent().getId())) {
                    array.remove(i);
                    arrayText.remove(i);
                    break;
                }
            }

            search.getChildren().remove(xButton.getParent());
        });

        hBox.getChildren().add(textField);
        hBox.getChildren().add(xButton);

        ++counting;
        hBox.setId("HBox"+counting);

//        Button addButton = (Button) loader.getNamespace().get("AddButton");
        search.getChildren().remove(addButton);
        search.getChildren().add(hBox);
        search.getChildren().add(addButton);
        array.add(hBox);
        arrayText.add(textField);
    }

    public void clickSearchButton() {
        if(array.isEmpty()) return ;
        StringBuilder text = new StringBuilder();

        for (TextField tf : arrayText) {
//            System.out.println(tf.getText());
            String txt = tf.getText();
            if(txt.isEmpty()) continue;
            text.append(txt);
            text.append("\n");
        }

        if(text.length() == 0) return ;
        startScraperHandler.startCrawl(false, text.toString());
    }

    public void clickResumeButton() {
        startScraperHandler.startCrawl(true,"");
    }

    public void clickAddButton(VBox search, Button addButton) {
        createMoreTextField(search, addButton);
    }

    public void clickUpload() {
        switchingScene.switchToAddFile();
    }

    public void clickStaticData() {
        switchingScene.switchToDisplayKOL();
    }

    public SearchingLogic(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        array = new ArrayList<>();
        arrayText = new ArrayList<>();

        startScraperHandler = new StartScraperHandler(switchingScene);
    }

    public void start(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
