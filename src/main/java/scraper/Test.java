package scraper;

import model.User;
import storage.main.StorageHandler;
import utils.ObjectType;

import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        StorageHandler storageHandler = new StorageHandler();
        System.out.println("Loading");
        storageHandler.load(ObjectType.USER, "KOLs.json");
        System.out.println("Loaded");
        List<User> users = storageHandler.getAll(ObjectType.USER, "KOLs.json")
                .stream()
                .filter(item -> item instanceof User)
                .map(item -> (User) item)
                .toList();

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
