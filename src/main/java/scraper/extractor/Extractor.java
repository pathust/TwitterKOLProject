package scraper.extractor;

import java.io.IOException;
import java.util.List;

public interface Extractor<T> {
    void extractData(String link) throws IOException, InterruptedException;
    List <T> extractItems(int maxListSize, boolean addToStorage) throws IOException, InterruptedException;
}
