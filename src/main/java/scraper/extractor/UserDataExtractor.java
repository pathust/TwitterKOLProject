package scraper.extractor;

import model.User;

import java.io.IOException;
import java.util.List;

public interface UserDataExtractor {
    void extractData(String userLink) throws IOException;
    List <User> extractUsers(boolean isVerified, int maxListSize, boolean addToStorage) throws IOException;
}
