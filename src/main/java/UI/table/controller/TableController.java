package UI.table.controller;

import UI.table.services.Filter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;

public abstract class TableController<T> {

    // Phương thức trừu tượng để tạo bảng
    protected abstract TableView<T> createTable(ObservableList<T> data);

    // Phương thức trừu tượng để hiển thị chi tiết
    protected abstract void showDetails(T item);

    // Tạo bảng với bộ lọc và xử lý nhấp chuột
    public VBox getTable(List<T> dataList, Function<T, String>... filterAttributes) {
        ObservableList<T> masterData = FXCollections.observableArrayList(dataList);

        // Tạo bảng
        TableView<T> tableView = createTable(masterData);

        // Tạo bộ lọc
        TextField searchField = new TextField();
        Filter.applyFilter(
                searchField,
                masterData,
                tableView,
                filterAttributes
        );

        // Xử lý sự kiện khi nhấp vào hàng
        setupRowClickHandler(tableView);

        // Layout tổng thể
        VBox layout = new VBox(10);
        layout.getChildren().addAll(searchField, tableView);

        return layout;
    }

    // Xử lý sự kiện khi nhấp vào hàng (chung)
    private void setupRowClickHandler(TableView<T> tableView) {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Nhấp đúp
                T selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showDetails(selectedItem); // Hiển thị chi tiết
                }
            }
        });
    }
}