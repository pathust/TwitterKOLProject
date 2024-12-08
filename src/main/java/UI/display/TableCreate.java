package UI.display;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import model.DataModel;

import java.util.function.Function;

public class TableCreate {

    // Hàm tạo cột chung
    public <T1, T> TableColumn<T1, T> createColumn(String title, Function<T1, T> mapper) {
        TableColumn<T1, T> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(mapper.apply(cellData.getValue()))
        );
        return column;
    }

    public <T> void setupRowClickHandler(TableView<T> tableView, java.util.function.Consumer<T> onClickAction) {
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    T clickedDataModel = row.getItem();
                    onClickAction.accept(clickedDataModel);
                }
            });
            return row;
        });
    }
}

