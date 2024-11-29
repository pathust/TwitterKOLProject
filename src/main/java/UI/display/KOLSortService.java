package UI.display;

import model.User;

import java.util.Comparator;
import java.util.List;

public class KOLSortService {

    public static void sortByUsername(List<User> kolList) {
        kolList.sort(Comparator.comparing(User::getUsername));
    }

    public static void sortByFollowersCount(List<User> kolList) {
        kolList.sort(Comparator.comparingInt(User::getFollowersCount));
    }
}
