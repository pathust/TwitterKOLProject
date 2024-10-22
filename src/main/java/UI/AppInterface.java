package UI;

import scraper.TwitterScraper;
import model.KOL;

import java.util.List;

import graph.Graph;
import graph.PagerankCalculator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppInterface extends Application {

    private TwitterScraper scraper;
    private Graph graph;
    private PagerankCalculator pagerankCalculator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        scraper = new TwitterScraper();
        graph = new Graph();
        pagerankCalculator = new PagerankCalculator();

        primaryStage.setTitle("KOL Finder");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Blockchain keyword");

        Button searchButton = new Button("Search KOL");
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();
            List<KOL> kolList = null;//scraper.searchKOLs(keyword);
            System.out.println(kolList.get(0));
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(searchField, searchButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
