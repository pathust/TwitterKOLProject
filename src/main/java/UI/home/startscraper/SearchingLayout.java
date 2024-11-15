package UI.home.startscraper;

import UI.SwitchingScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class SearchingLayout {
    private HBox searchingComponent;
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

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(10);
        layout.setVgap(15);

        layout.setPadding(new Insets(25, 25, 25, 25));

        layout.add(instruction, 0,0);
        layout.add(searchField, 0, 1);
        layout.add(searchButton, 0, 2);

        searchingComponent = new HBox(layout);

        startScraperHandler = new StartScraperHandler(switchingScene);
        searchButton.setOnAction(event -> {
            StartSearch(searchField.getText());
        });
    }

    public HBox getSearchComponent() {
        return searchingComponent;
    }
}
