package UI.home;

import UI.SwitchingScene;
import UI.home.addfile.AddFile;
import UI.home.startscraper.SearchingLayout;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class HomeScene{
    private AddFile addFile;
    private SearchingLayout searchingLayout;
    private Scene scene;
    private Stage stage;
    private ScrollPane root;

    public HomeScene(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        addFile = new AddFile(stage);
        searchingLayout = new SearchingLayout(switching);

        HBox addFileComponent = addFile.getComponent();
        Pane searchingComponent = searchingLayout.getSearchComponent();

        VBox vBox = new VBox(searchingComponent, addFileComponent);
        vBox.setStyle("-fx-background-color: green;");

        vBox.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));
        vBox.prefHeightProperty().bind(stage.heightProperty().multiply(0.8));

        VBox.setVgrow(searchingComponent, Priority.ALWAYS);

        root = new ScrollPane();
        root.setContent(vBox);

//        root.setStyle("-fx-alignment: center;");
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

//        vBox.layoutXProperty().bind((stage.widthProperty().subtract(vBox.prefWidthProperty())).divide(2));
//        vBox.layoutYProperty().bind((stage.heightProperty().subtract(vBox.prefHeightProperty())).divide(2));
//
//        stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
//            vBox.layoutXProperty().bind((stage.widthProperty().subtract(vBox.prefWidthProperty())).divide(2));
//        });
//
//        stage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
//            vBox.layoutYProperty().bind((stage.heightProperty().subtract(vBox.prefHeightProperty())));
//        });

        scene = new Scene(root, 600, 600);
        stage.setScene(scene);
    }

    public void start() {
        this.stage.show();
    }

    public void close() {
        this.stage.close();
    }
}
