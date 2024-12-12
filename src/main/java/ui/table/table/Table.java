package ui.table.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public abstract class Table<T> {

    // Phương thức tạo bảng chung
    public TableView<T> createTable(ObservableList<T> data) {
        TableView<T> tableView = new TableView<>();
        tableView.setItems(data);

        // Thêm các cột vào bảng
        tableView.getColumns().addAll(getColumns());
        return tableView;
    }

    // Phương thức trừu tượng, các lớp con sẽ phải cài đặt cột của riêng mình
    protected abstract java.util.List<TableColumn<T, ?>> getColumns();

    // Phương thức tạo cột chung
    protected <S> TableColumn<T, S> createColumn(String title, String property) {
        TableColumn<T, S> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));

        // Nếu là số thực (Float/Double), nhân lên 10^5 và định dạng hiển thị
        column.setCellFactory(col -> new TableCell<T, S>() {
            @Override
            protected void updateItem(S item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item instanceof Float || item instanceof Double) {
                    double scaledValue = ((Number) item).doubleValue() * 100_000;
                    setText(String.format("%.4f", scaledValue));
                } else {
                    setText(item.toString());
                }
            }
        });

        return column;
    }
}