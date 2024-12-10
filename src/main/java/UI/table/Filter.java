package UI.table;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Function;

public class Filter {

    public static <T> FilteredList<T> applyFilter(
            TextField searchField,
            ObservableList<T> masterData,
            TableView<T> tableView,
            Function<T, String>... attributesToSearch) {

        // Create a FilteredList for the given data
        FilteredList<T> filteredData = new FilteredList<>(masterData, p -> true);

        // Add a listener to filter data when the search text changes
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Check all specified attributes for a match
                for (Function<T, String> attribute : attributesToSearch) {
                    String value = attribute.apply(item);
                    if (value != null && value.toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }
                return false;
            });
        });

        // Wrap the filtered data in a SortedList for sorting
        SortedList<T> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Set the TableView items to the sorted and filtered data
        tableView.setItems(sortedData);

        return filteredData;
    }
}
