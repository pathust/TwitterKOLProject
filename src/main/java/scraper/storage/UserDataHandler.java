package scraper.storage;

import model.Tweet;
import model.User;

import java.io.IOException;
import java.util.List;

public interface UserDataHandler {
    void loadUsers(String filePath) throws IOException;
    void addUser(String filePath, User newUser) throws IOException;
    void saveData(String filePath) throws IOException;
    List<User> getUsers(String filePath) throws IOException;
    boolean userExists(String filePath, String profileLink) throws IOException;
}
