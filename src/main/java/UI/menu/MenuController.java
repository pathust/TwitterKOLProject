package UI.menu;

import UI.SwitchingScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController {
    private SwitchingScene switchingScene;
    private static Button crawl, upload, staticData;
    FXMLLoader loader;

    public MenuController() {}

    public MenuController(SwitchingScene switching) {
        switchingScene = switching;
        System.out.println(switching);

        loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("No FIle found");
            throw new RuntimeException(e);
        }

        crawl = (Button) loader.getNamespace().get("Crawl");
        System.out.println(crawl);

        crawl.setOnAction(event -> {
//            System.out.println("Hello");
            switchingScene.switchToSearching();
        });

        upload = (Button) loader.getNamespace().get("Upload");
        System.out.println(upload);

        upload.setOnAction(event -> {
            System.out.println("Hello");
            switchingScene.switchToAddFile();
        });
    }
}
