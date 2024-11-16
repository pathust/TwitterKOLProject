package UI.display;

import UI.SwitchingScene;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.List;

public class DisplayScene  {
    private Stage stage;
    private SwitchingScene switchingScene;
    private Scene displayScene;

    public DisplayScene(Stage primaryStage, SwitchingScene switching) {
        stage = primaryStage;
        switchingScene = switching;

        stage.setTitle("KOL Found list");
    }

    public void init() {
        try {
            List<User> listUser = JsonReader.readAccountsFromJson("KOLs.json");
            System.out.print(listUser.size());

            VBox view = new VBox(20);
            view.setPrefHeight(400);
            ScrollPane scrollPane = new ScrollPane(view);

            for(User user: listUser) {
                Label name = new Label("Name: " + user.getUsername());
                Label followers = new Label("Followers: " + user.getFollowersCount());
                Label link = new Label("Link: " + user.getProfileLink());

//                System.out.println(user.getUsername());
                VBox acc = new VBox(10, name, followers, link);
                view.getChildren().add(acc);
            }

            scrollPane.setPrefHeight(400);
            displayScene = new Scene(scrollPane, 500, 500);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Error when read Json File");
        }
    }

    public void start(){
//        System.out.print("1");
        init();
        stage.setScene(displayScene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
