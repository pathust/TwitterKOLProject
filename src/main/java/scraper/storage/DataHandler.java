package scraper.storage;

import model.User;

import java.io.IOException;
import java.util.List;

public interface DataHandler {
    void addUser(String filePath, User newUser) throws IOException;
    void saveData(String filePath) throws IOException;
    List<String> getUserLinksFrom(String filePath) throws IOException;
}
