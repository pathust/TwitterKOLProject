package UI.table.services;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.net.URI;

public class LinkCellFactory {

    public static <T> void applyLinkToColumn(TableColumn<T, String> column) {
        column.setCellFactory(col -> new TableCell<T, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Hyperlink link = new Hyperlink("Link");
                    link.setOnAction(e -> {
                        try {
                            // Mở liên kết trong trình duyệt mặc định
                            java.awt.Desktop.getDesktop().browse(new URI(item));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    setGraphic(link);
                }
            }
        });
    }
}