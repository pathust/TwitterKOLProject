package scraper.extractor;

import java.io.IOException;
import java.util.List;

public interface Extractor<T> {
    void extractData(String filePath, String key) throws IOException, InterruptedException;
    List <T> extractItems(String filePath, int maxListSize, boolean addToStorage) throws IOException, InterruptedException;
}
