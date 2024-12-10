package UI.table.services;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Function;

public class Filter {

    // Hàm áp dụng cả lọc tìm kiếm và sắp xếp
    public static <T> FilteredList<T> applyFilter(
            TextField searchField,
            ObservableList<T> masterData,
            TableView<T> tableView,
            Function<T, String>... attributesToSearch) {

        // Tạo FilteredList từ dữ liệu ban đầu
        FilteredList<T> filteredData = new FilteredList<>(masterData, p -> true);

        // Áp dụng lọc tìm kiếm
        SearchFilter.applySearchFilter(searchField, masterData, tableView, attributesToSearch);

        // Áp dụng lọc sắp xếp
        SortFilter.applySortFilter(tableView);

        // Cập nhật lại dữ liệu lọc trong bảng
        tableView.setItems(filteredData);

        System.out.println(masterData.toString());
        return filteredData;
    }
}