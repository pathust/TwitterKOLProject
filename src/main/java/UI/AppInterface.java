package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import scraper.TwitterScraperController;
import model.KOL;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Task;

import graph.Graph;
import graph.PagerankCalculator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppInterface extends Application {

    private TwitterScraperController scraper;
    private Graph graph;
    private PagerankCalculator pagerankCalculator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        graph = new Graph();
        pagerankCalculator = new PagerankCalculator();

        primaryStage.setTitle("KOL Finder");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Blockchain keyword");

        Button searchButton = new Button("Search KOL");
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();

            if(keyword.isEmpty()) {
                return ;
            }

            WaitingScene waiting = new WaitingScene(primaryStage);
            waiting.start();

            Task<Void> seleniumTask  = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        String[] otherArgs = {keyword};
                        scraper.main(otherArgs);

                        scraper.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    return null;
                }

                @Override
                protected void succeeded() {
                    waiting.close();
                }
            };

            Thread thread  = new Thread(seleniumTask);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            waiting.close();
            List<KOL> kolList = null;//scraper.searchKOLs(keyword);
//            System.out.println(kolList.get(0));
        });

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(10);
        layout.setVgap(15);

        layout.setPadding(new Insets(25, 25, 25, 25));

        layout.add(searchField, 0, 0);
        layout.add(searchButton, 0, 1);

        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
