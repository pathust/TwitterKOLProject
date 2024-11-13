package graph;

import model.GraphNode;
import model.User;
import scraper.storage.UserDataHandler;
import scraper.storage.UserStorageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserNodeExtract {
    public static List<GraphNode> extract() throws IOException {
        UserDataHandler userDataHandler = new UserStorageManager();
        userDataHandler.loadUsers("KOLs.json");
        List<User> userList = userDataHandler.getUsers("KOLs.json");

        List<GraphNode> nodeList = new ArrayList<>();

        for (User user : userList) {
            nodeList.add(new GraphNode(user));
        }

        return nodeList;
    }

}
