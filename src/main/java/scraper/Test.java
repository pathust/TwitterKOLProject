package scraper;

import model.User;
import scraper.storage.StorageHandler;
import scraper.storage.UserStorageManager;

import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        StorageHandler<User> userDataHandler = new UserStorageManager();
        System.out.println("Loading");
        userDataHandler.load("KOLs.json");
        System.out.println("Loaded");
        List<User> users = userDataHandler.getAll("KOLs.json");
        System.out.println(users.size());
        int sum = 0;
        for (User user : users) {
            System.out.println(sum);
            List<String> list = user.getFollowersList();
            String name = user.getUsername();
            System.out.println("Name: " + name);
            if(list.size() > 3){
                sum++;
            }
        }

        System.out.println("Sum: " + sum);

    }
}
