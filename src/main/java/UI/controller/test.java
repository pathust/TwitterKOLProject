package UI.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class test extends Application {
   @Override
   public void start(Stage primaryStage) throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/started.fxml"));
       Parent root = loader.load();

       Scene scene = new Scene(root);
       primaryStage.initStyle(StageStyle.DECORATED);
       primaryStage.setScene(scene);
       primaryStage.setTitle("Twitter KOL Project");
       primaryStage.show();
   }
   public static void main(String[] args) {
      launch(args);
   }
}
