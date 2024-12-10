package UI.table.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Function;

public class SearchFilter {

    // Hàm lọc tìm kiếm theo thuộc tính của đối tượng
    public static <T> void applySearchFilter(
            TextField searchField,
            ObservableList<T> masterData,    // Dữ liệu gốc
            TableView<T> tableView,
            Function<T, String>... attributesToSearch) {

        // Lắng nghe sự thay đổi trong trường tìm kiếm
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Nếu không có nội dung tìm kiếm, hiển thị lại toàn bộ dữ liệu gốc
                tableView.setItems(masterData);  // Đặt lại toàn bộ dữ liệu cho bảng
            } else {
                String lowerCaseFilter = newValue.toLowerCase();

                // Lọc danh sách theo các thuộc tính tìm kiếm
                ObservableList<T> filteredList = FXCollections.observableArrayList();
                for (T item : masterData) {
                    // Duyệt qua tất cả các thuộc tính cần tìm kiếm
                    for (Function<T, String> attribute : attributesToSearch) {
                        String value = attribute.apply(item);
                        // Kiểm tra nếu giá trị không null và khớp với từ khóa tìm kiếm
                        if (value != null && value.toLowerCase().contains(lowerCaseFilter)) {
                            filteredList.add(item);  // Thêm vào danh sách kết quả nếu khớp
                            break;  // Nếu đã tìm thấy, không cần kiểm tra các thuộc tính khác
                        }
                    }
                }
                // Cập nhật lại bảng với các kết quả tìm kiếm
                tableView.setItems(filteredList);
            }
        });
    }
}