package scraper.extractor;

import java.io.IOException;
import java.util.List;

public interface UserDataExtractor {
    void extractData(String userLink) throws InterruptedException, IOException;
    void extractUserTo(String filePath, boolean isVerified) throws InterruptedException, IOException;
}
