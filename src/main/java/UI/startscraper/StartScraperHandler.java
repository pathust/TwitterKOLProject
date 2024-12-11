package UI.startscraper;

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
    private SwitchingScene switchingScene;
    private String text;

    private List<String> filter() {
        List<String> lines = Arrays.stream(text.split("\\r?\\n"))
                                   .map(String::trim) // Xóa khoảng trắng ở đầu và cuối
                                   .filter(line -> !line.isEmpty()) // Loại bỏ dòng trống
                                   .collect(Collectors.toList());
        System.out.println(lines.size());
        return lines;
    }

    private Task<Void> initTask(boolean resume) {
        Task<Void> scraper = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    if(filter().size() == 0) return null;

                    TwitterScraperController.main(resume, filter().toArray(new String[0]));

                    Platform.runLater(() -> {
                        switchingScene.closeWaiting();
                        switchingScene.switchToDisplayKOL();
                    });
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                return null;
            }
        };
        return scraper;
    }

    public void startCrawl(boolean resume, String searchingText) {
        crawler = new Thread(initTask(resume));
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
    }
}
