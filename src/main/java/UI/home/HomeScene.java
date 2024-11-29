package UI.home;

import UI.SwitchingScene;
import UI.home.addfile.AddFile;
import UI.home.startscraper.SearchingLayout;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeScene{
    private AddFile addFile;
    private SearchingLayout searchingLayout;
    private Scene scene;
    private Stage stage;

    public HomeScene() {

    }

    public HomeScene(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        addFile = new AddFile(stage);
        searchingLayout = new SearchingLayout(switching);

        HBox addFileComponent = addFile.getComponent();
        HBox searchingComponent = searchingLayout.getSearchComponent();

        VBox vBox = new VBox(searchingComponent, addFileComponent);
        scene = new Scene(vBox, 500, 500);

        stage.setScene(scene);
    }

    public void start() {
        this.stage.show();
    }

    public void close() {
        this.stage.close();
    }
}
