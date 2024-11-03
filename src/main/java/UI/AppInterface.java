package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import model.GraphNode;
import scraper.TwitterScraperController;

import java.io.IOException;
import java.util.List;
import javafx.concurrent.Task;

import graph.Graph;
import graph.PagerankCalculator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AppInterface extends Application {

    private TwitterScraperController scraper;
    private Graph graph;
    private PagerankCalculator pagerankCalculator;
    private int previousSize = -1;
//    private Timeline timeline;
//    private boolean shouldStop = false;

//    private void checkJsonFile() {
//        int size = 0;
//
//        try {
//            File jsonFile = new File("KOLs.json");
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonNode = mapper.readTree(jsonFile);
//
//            if(jsonNode.isObject()) {
//                size = jsonNode.size();
//            } else if(jsonNode.isArray()) {
//                size = jsonNode.size();
//            }
//
//            if(previousSize == size) {
//                shouldStop = true;
//                timeline.stop();
//                return ;
//            }
//
//            previousSize = size;
//            System.out.println(previousSize);
//        } catch (IOException ex) {
//            System.out.println("Can't read the Json File !");
//        }
//    }

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
//                        previousSize = -1;
                        scraper = new TwitterScraperController();
                        String[] otherArgs = {keyword};
                        scraper.main(otherArgs);

                        waiting.close();
                        scraper.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
//                    catch (InterruptedException ex) {
//                        throw new RuntimeException(ex);
//                    }

                    return null;
                }

                @Override
                protected void succeeded() {
//                    Stage stage = (Stage) getWindow();
                    //System.out.println("Your Data have been crawled");
                }
            };

            new Thread(seleniumTask).start();
            List<GraphNode> kolList = null;//scraper.searchKOLs(keyword);
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
