package UI.table.services;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

import java.util.function.Function;

public class SearchFilter {

    public static <T> FilteredList<T> applySearchFilter(
            TextField searchField,
            FilteredList<T> filteredData,
            Function<T, String>... attributesToSearch) {

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Không có bộ lọc, hiển thị tất cả
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Kiểm tra tất cả các thuộc tính cần tìm kiếm
                for (Function<T, String> attribute : attributesToSearch) {
                    String value = attribute.apply(item);
                    if (value != null && value.toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }
                return false; // Không khớp
            });
        });

        return filteredData;
    }
}