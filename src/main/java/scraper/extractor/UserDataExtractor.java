package scraper.extractor;

import model.User;

import java.io.IOException;
import java.util.List;

public interface UserDataExtractor {
    void extractData(String userLink) throws InterruptedException;
    List <User> extractUsers(boolean isVerified, int maxListSize) throws InterruptedException;
}
