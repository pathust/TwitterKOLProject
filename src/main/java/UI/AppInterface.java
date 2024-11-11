package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import scraper.TwitterScraperController;
import model.KOL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.application.Platform;

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

public class AppInterface extends Application implements HomeScene.ButtonClickListener{
    private Graph graph;
    private PagerankCalculator pagerankCalculator;
    private HomeScene homeScene;
    private WaitingScene waitingScene;
    private TwitterScraperController twitterScraper;

    private void startScraper() {
        Task<Void> scraper = new Task<>() {
            @Override
            protected Void call() {
                try {
                    String text = homeScene.getText();
                    String[] textList = {text};
//                    twitterScraper = new TwitterScraperController();
//                    System.out.println(text);
                    TwitterScraperController.main(textList);

                    Platform.runLater(() -> {
                        waitingScene.close();
                    });
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                return null;
            }
        };

        new Thread(scraper).start();
    }

    @Override
    public void onButtonClicked() {
        if(homeScene.getText().isEmpty()) {
            return ;
        }

        waitingScene.start();
        startScraper();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        graph = new Graph();
        pagerankCalculator = new PagerankCalculator();

        homeScene = new HomeScene(primaryStage);
        waitingScene = new WaitingScene(primaryStage);

        homeScene.setButtonClickListener(this);
        homeScene.setUpButton();
        homeScene.start();

//        AddFile add = new AddFile();
//        add.start(primaryStage);
    }
}
