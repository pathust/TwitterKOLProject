package UI;

import UI.FileAdding.AddFileHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeScene{
    private TextArea searchField;
    private Button searchButton;
    private GridPane layout;
    private Scene scene;
    private Stage stage;
    private AddFileHandler addFileHandler;

    public interface ButtonClickListener {
        void onButtonClicked();
    }

    private ButtonClickListener listener;

    public void setButtonClickListener(ButtonClickListener listeners) {
        this.listener = listeners;
    }

    public void setUpButton() {
        this.searchButton.setOnAction(event -> {
            listener.onButtonClicked();
        });
    }

    HomeScene(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("KOL Finder");

        this.searchField = new TextArea();
        this.searchField.setPromptText("Enter Blockchain keyword");

        this.searchButton = new Button("Search KOL");

        this.layout = new GridPane();
        this.layout.setAlignment(Pos.CENTER);
        this.layout.setHgap(10);
        this.layout.setVgap(15);

        this.layout.setPadding(new Insets(25, 25, 25, 25));

        this.layout.add(searchField, 0, 0);
        this.layout.add(searchButton, 0, 1);

        addFileHandler = new AddFileHandler(primaryStage);
        Button addFileButton = new Button("Add JSON File");
        Label fileLabel = new Label("No file selected");

        // Xử lý khi bấm nút "Add JSON File"
        addFileButton.setOnAction(event -> {
            File selectedFile = addFileHandler.openFileDialog();
            if (selectedFile != null) {
                fileLabel.setText("Selected file: " + selectedFile.getAbsolutePath());
                // Xử lý tệp JSON nếu cần (đọc hoặc lưu đường dẫn)
            }
        });

        HBox addFile = new HBox(10, addFileButton, fileLabel);
        this.layout.add(addFile, 0, 2);

        this.scene = new Scene(layout, 500, 500);
    }

    public String getText() {
        return this.searchField.getText();
    }

    public void start() {
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public void close() {
        this.stage.close();
    }
}
