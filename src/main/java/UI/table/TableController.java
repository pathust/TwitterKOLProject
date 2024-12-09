package UI.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Function;

public abstract class TableController<T> {

    // Abstract method: Lớp con phải triển khai
    protected abstract TableView<T> createTable(ObservableList<T> data);

    protected abstract void showDetails(T item);

    // Tạo bảng với bộ lọc và xử lý hàng
    public VBox getTable(List<T> dataList, Function<T, String>... filterAttributes) {
        // Dữ liệu chính
        ObservableList<T> masterData = FXCollections.observableArrayList(dataList);

        // Tạo bảng
        TableView<T> tableView = createTable(masterData);

        // Tạo bộ lọc
        Filter filter = new Filter();
        TextField searchField = new TextField();
        FilteredList<T> filteredData = filter.applyFilter(
                searchField,
                masterData,
                tableView,
                filterAttributes // Các thuộc tính cần lọc
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
