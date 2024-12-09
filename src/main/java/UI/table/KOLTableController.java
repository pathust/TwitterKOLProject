package UI.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.User;

import java.util.List;

public class KOLTableController extends TableController<User> {

    @Override
    protected TableView<User> createTable(ObservableList<User> data) {
        KOLTable kolTable = new KOLTable();
        return kolTable.createTable(data);
    }

    @Override
    protected void showDetails(User user) {
        Details details = new Details();
        details.showDetails(user); // Hiển thị thông tin chi tiết của User
    }

    public VBox getTable(List<User> kolList) {
        return super.getTable(kolList, User::getUsername, User::getProfileLink);
    }
}
