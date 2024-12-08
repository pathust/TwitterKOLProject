package UI.display;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Tweet;
import model.User;

import java.lang.reflect.Field;

public class Details {

    public void showDetails(User user) {
        Stage stage = new Stage();
        stage.setTitle("User Details");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Username: " + user.getUsername()),
                new Label("Profile Link: " + user.getProfileLink()),
                new Label("Followers Count: " + user.getFollowersCount()),
                new Label("Following Count: " + user.getFollowingCount())
        );

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public void showDetails(Tweet tweet) {
        Stage stage = new Stage();
        stage.setTitle("Tweet Details");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("User name: " + tweet.getAuthorUsername()),
                new Label("Profile Link: " + tweet.getAuthorProfileLink()),
                new Label("Tweet Link: " + tweet.getTweetLink()),
                new Label("Repost Count: " + tweet.getRepostCount())
        );

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
//    public <T> void showDetails(T item) {
//        Stage stage = new Stage();
//        stage.setTitle(item.getClass().getSimpleName() + " Details");
//
//        VBox vbox = new VBox(10);
//
//        // Use reflection to get all fields of the class
//        for (Field field : item.getClass().getDeclaredFields()) {
//            field.setAccessible(true); // Allow access to private fields
//            try {
//                Object value = field.get(item); // Get the value of the field
//                String labelText = field.getName() + ": " + (value != null ? value.toString() : "null");
//                vbox.getChildren().add(new Label(labelText));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Scene scene = new Scene(vbox, 300, 200);
//        stage.setScene(scene);
//        stage.show();
//    }
}
