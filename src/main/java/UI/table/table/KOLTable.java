package UI.table.table;

import javafx.scene.control.TableColumn;
import model.User;
import UI.table.services.LinkCellFactory;

import java.util.ArrayList;
import java.util.List;

public class KOLTable extends Table<User> {

    @Override
    protected List<TableColumn<User, ?>> getColumns() {
        List<TableColumn<User, ?>> columns = new ArrayList<>();

        // Cột Username
        TableColumn<User, String> usernameCol = createColumn("Username", "username");

        // Cột Profile Link (dùng LinkCellFactory để xử lý các liên kết)
        TableColumn<User, String> profileLinkCol = createColumn("Profile Link", "profileLink");
        LinkCellFactory.applyLinkToColumn(profileLinkCol);

        // Cột Following Count
        TableColumn<User, Integer> followingCountCol = createColumn("Following Count", "followingCount");

        // Cột Followers Count
        TableColumn<User, Integer> followersCountCol = createColumn("Followers Count", "followersCount");

        // Cột Pagerank
        TableColumn<User, Double> pagerankCol = createColumn("PagerankScore", "pagerankScore");

        columns.add(usernameCol);
        columns.add(profileLinkCol);
        columns.add(followingCountCol);
        columns.add(followersCountCol);
        columns.add(pagerankCol);

        return columns;
    }
}