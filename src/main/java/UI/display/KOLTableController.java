package UI.display;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.User;

public class KOLTableController {
    private FilteredList<User> filteredData;

    public void setupFilterAndSort(TextField searchField, TableView<User> tableView, ObservableList<User> masterData) {
        filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                        user.getProfileLink().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

}