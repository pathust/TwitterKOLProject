package UI.home.startscraper;

import UI.SwitchingScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SearchingLayout {
    private Pane searchingComponent;
    private StartScraperHandler startScraperHandler;

    private void StartSearch(String text) {
        startScraperHandler.startCrawl(text);
    }

    public SearchingLayout(SwitchingScene switchingScene) {
        Label instruction = new Label("Enter keywords here. If there are multiple keywords," +
                                        "enter each keyword on a separate line.");

        instruction.setWrapText(true);

        TextArea searchField = new TextArea();
        searchField.setPromptText("Enter Blockchain keyword");

        Button searchButton = new Button("Search KOL");

        startScraperHandler = new StartScraperHandler(switchingScene);
        searchButton.setOnAction(event -> {
            StartSearch(searchField.getText());
        });

        Button addTextField = new Button("New TextField");

        VBox layout = new VBox(searchField, searchButton, addTextField);

        addTextField.setOnAction(event -> {
            TextArea trt = new TextArea();
            layout.getChildren().add(trt);
        });

        searchingComponent = new Pane(layout);
    }

    public Pane getSearchComponent() {
        return searchingComponent;
    }
}
