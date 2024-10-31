package scraper;

import model.User;
import scraper.storage.UserDataHandler;
import scraper.storage.UserStorageManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) throws IOException {
        UserDataHandler userDataHandler = new UserStorageManager();
        System.out.println("Loading");
        userDataHandler.loadUsers("KOLs.json");
        System.out.println("Loaded");
        List<User> users = userDataHandler.getUsers("KOLs.json");
        System.out.println(users.size());
        for (User user : users) {
            System.out.println("User: " + user.getUsername());
            String followingList = user
                    .getFollowingList()
                    .stream()
                    .map(User::getUsername)
                    .collect(Collectors.joining(", "));
            System.out.println("Following List: " + followingList);
        }

    }
}
