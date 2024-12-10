package UI.table.services;

import javafx.scene.control.TableView;

public class SortFilter {

    // Hàm lọc sắp xếp bảng
    public static <T> void applySortFilter(TableView<T> tableView) {
        // Duy trì thứ tự sắp xếp mặc định của bảng
        tableView.getSortOrder().clear();
    }
}