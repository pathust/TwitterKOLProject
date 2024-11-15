package UI.home.startscraper;

import UI.SwitchingScene;
import javafx.application.Platform;
import javafx.concurrent.Task;
import scraper.TwitterScraperController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StartScraperHandler {
    private static Thread crawler;
    private Task<Void> scraper;
    private SwitchingScene switchingScene;
    private String text;

    private List<String> filter() {
        List<String> lines = Arrays.stream(text.split("\\r?\\n"))
                                   .map(String::trim) // Xóa khoảng trắng ở đầu và cuối
                                   .filter(line -> !line.isEmpty()) // Loại bỏ dòng trống
                                   .collect(Collectors.toList());
        return lines;
    }

    private void initTask() {
        scraper = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    if(filter().isEmpty()) return null;
                    TwitterScraperController.main(filter().toArray(new String[0]));

                    Platform.runLater(() -> {
                        switchingScene.closeWaiting();
                        switchingScene.switchToDisplay();
                    });
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                return null;
            }
        };
    }

    public void startCrawl(String searchingText) {
        text = searchingText;
        crawler.start();
        switchingScene.switchToWaiting();
    }

    public static void closeThread() {
        TwitterScraperController.close();
        crawler.interrupt();
    }

    public StartScraperHandler(SwitchingScene switching) {
        this.switchingScene = switching;
        initTask();
        crawler = new Thread(scraper);
    }
}
