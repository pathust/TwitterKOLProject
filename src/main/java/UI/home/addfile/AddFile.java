package UI.home.addfile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.File;

public class AddFile {
    private HBox addFileComponent;
    private AddFileHandler addFileHandler;

    void setChooseFileButton(Button button, Label label) {
        button.setOnAction(event -> {
            File selectedFile = addFileHandler.openFileDialog();
            if (selectedFile != null) {
                label.setText("Selected file: " + selectedFile.getAbsolutePath());
            }
        });
    }

    void setAddFileButton(Button button) {
        button.setOnAction(event -> {
            addFileHandler.copyFile();
        });
    }

    public AddFile(Stage stage) {
        Label instruction = new Label("Or choose a Json File");
        instruction.setWrapText(true);

        Button chooseFileButton = new Button("Choose a JSON File");
        Label fileLabel = new Label("No file selected");
        Button addFileButton = new Button("Add File");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        grid.add(instruction, 0, 0);
        grid.add(fileLabel, 0, 1);
        grid.add(chooseFileButton, 1, 1);
        grid.add(addFileButton, 0, 2);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(30);
        grid.getColumnConstraints().addAll(col1, col2);

        chooseFileButton.setMinWidth(Region.USE_PREF_SIZE);

//        grid.setVgap(20); grid.setHgap(20);
        addFileHandler = new AddFileHandler(stage);

//        grid.setPadding(new Insets(25, 25, 25, 25));

        setChooseFileButton(chooseFileButton, fileLabel);
        setAddFileButton(addFileButton);

        addFileComponent = new HBox(grid);
        addFileComponent.setAlignment(Pos.CENTER);
    }

    public HBox getComponent() {
        return addFileComponent;
    }
}
