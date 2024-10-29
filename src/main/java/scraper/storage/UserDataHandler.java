package scraper.storage;

import model.Tweet;
import model.User;

import java.io.IOException;
import java.util.List;

public interface UserDataHandler {
    void addUser(String filePath, User newUser) throws IOException;
    void saveData(String filePath) throws IOException;
    List<String> getUserLinksFrom(String filePath) throws IOException;
}
