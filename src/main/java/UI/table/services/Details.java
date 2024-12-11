package UI.table.services;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DataModel;
import model.Tweet;
import model.User;

public class Details {

    public void showDetails(User user) {
        Stage stage = new Stage();
        stage.setTitle("User Details");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Username: " + user.getUsername()),
                new Label("Profile Link: " + user.getProfileLink()),
                new Label("Followers Count: " + user.getFollowersCount()),
                new Label("Following Count: " + user.getFollowingCount()),
                new Label("Pagerank: " + user.getPagerankScore())
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
                new Label("Repost Count: " + tweet.getRepostCount()),
                new Label("Pagerank: " + tweet.getPagerankScore())
        );

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

        public void showDetails(DataModel dataModel) {
        Stage stage = new Stage();
        stage.setTitle("DataModel Details");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Unique Key: " + dataModel.getUniqueKey()),
                new Label("Pagerank: " + dataModel.getPagerankScore())
        );

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
