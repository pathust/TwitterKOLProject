package UI.table.services;

import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;

public class SortFilter {

    public static <T> SortedList<T> applySortFilter(
            SortedList<T> sortedData,
            TableView<T> tableView) {

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        return sortedData;
    }
}