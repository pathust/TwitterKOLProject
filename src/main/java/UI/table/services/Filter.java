package UI.table.services;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Function;

public class Filter {

    public static <T> void applyFilter(
            TextField searchField,
            ObservableList<T> masterData,
            TableView<T> tableView,
            Function<T, String>... attributesToSearch) {

        // Tạo FilteredList từ masterData
        FilteredList<T> filteredData = new FilteredList<>(masterData, p -> true);

//        System.out.println(attributesToSearch.length);
        // Áp dụng tìm kiếm
        SearchFilter.applySearchFilter(searchField, filteredData, attributesToSearch);

        // Tạo SortedList từ FilteredList
        SortedList<T> sortedData = new SortedList<>(filteredData);

        // Áp dụng sắp xếp
        SortFilter.applySortFilter(sortedData, tableView);

        // Đặt dữ liệu đã lọc và sắp xếp vào TableView
        tableView.setItems(sortedData);
    }
}