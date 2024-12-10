package UI.addfile;

import UI.SwitchingScene;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;

public class UploadFileLogic {
    private Stage stage;
    private SwitchingScene switchingScene;
    private AddFileHandler addFileHandler;

    public void clickChooseFile(TextField textField) {
        File selectedFile = addFileHandler.openFileDialog();

        if(selectedFile != null) {
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void clickAddKOL() {
        addFileHandler.copyFile("\\KOLs");
    }

    public void clickAddTweet() {
        addFileHandler.copyFile("\\Tweet");
    }

    public void clickCrawl() {
        switchingScene.switchToSearching();
    }

    public void clickStatic() {
        switchingScene.switchToDisplayTweet();
    }

    public void start(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    public UploadFileLogic(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        addFileHandler = new AddFileHandler(stage);
    }
}
