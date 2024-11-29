package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class test extends Application {
   @Override
   public void start(Stage primaryStage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("/main/java/UI/startedScene/started.fxml"));

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
