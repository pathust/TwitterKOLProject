package UI.display;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.User;

import java.util.List;

public class KOLTableController {

    public VBox getTable(List<User> kolList) {
        // Dữ liệu chính
        ObservableList<User> masterData = FXCollections.observableArrayList(kolList);

        // Lớp tạo bảng
        KOLTable kolTable = new KOLTable();
        TableView<User> tableView = kolTable.createTable(masterData);

        // Lớp bộ lọc
        Filter kolFilter = new Filter();
        TextField searchField = new TextField();
        FilteredList<User> filteredData = kolFilter.applyFilter(
                searchField,         // TextField for filtering
                masterData,      // Master list of users
                tableView,       // TableView for displaying the users
                User::getUsername,   // Attribute to search (username)
                User::getProfileLink // Attribute to search (profile link)
        );

        // Lớp hiển thị chi tiết
        Details kolDetails = new Details();
        kolTable.setupRowClickHandler(tableView, kolDetails::showDetails);

        // Layout tổng thể
        VBox layout = new VBox(10);
        layout.getChildren().addAll(searchField, tableView);

        return layout;
    }
}
