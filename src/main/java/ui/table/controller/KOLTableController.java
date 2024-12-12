package ui.table.controller;

import ui.table.services.Details;
import ui.table.table.KOLTable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.User;

import java.util.List;

public class KOLTableController extends TableController<User> {

    @Override
    protected TableView<User> createTable(ObservableList<User> data) {
        KOLTable kolTable = new KOLTable();
        return kolTable.createTable(data);  // Sử dụng KOLTable để tạo bảng
    }

    @Override
    protected void showDetails(User user) {
        Details details = new Details();
        details.showDetails(user);  // Hiển thị chi tiết của User
    }

    // Tạo bảng với bộ lọc và thuộc tính cần lọc
    public VBox getTable(List<User> kolList) {
        return super.getTable(kolList, User::getUsername, User::getProfileLink);
    }
}