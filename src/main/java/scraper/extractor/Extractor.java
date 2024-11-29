package scraper.extractor;

import java.io.IOException;
import java.util.List;

public interface Extractor<T> {
    void extractData(String link) throws IOException;
    List <T> extractItems(int maxListSize, boolean addToStorage) throws IOException;
}
