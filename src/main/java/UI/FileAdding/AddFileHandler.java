package UI.FileAdding;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AddFileHandler{
    Stage stage;
    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

    public AddFileHandler(Stage newStage) {
        this.stage = newStage;

        try {
            // Thiết lập FileHandler cho Logger để ghi log vào file
            FileHandler fileHandler = new FileHandler("fileSelection.log", true); // true để ghi tiếp vào log hiện tại
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println("Lỗi khi thiết lập FileHandler: " + e.getMessage());
        }
    }

    public File openFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile =  fileChooser.showOpenDialog(stage);

        // Ghi lại thông tin về việc chọn file
        if (selectedFile != null) {
            logger.info("User selected file: " + selectedFile.getAbsolutePath());
            // Sao chép file vào thư mục đích
            copyFile(selectedFile);
        } else {
            logger.info("User cancelled file selection.");
        }

        return selectedFile;
    }

    private void copyFile(File sourceFile) {
        // Xác định đường dẫn file đích
        File destinationFile = new File("D:\\GitHub\\TwitterKOLProject\\" + sourceFile.getName());

        try {
            // Nếu thư mục đích chưa tồn tại, tạo thư mục
            Path destinationPath = destinationFile.toPath();
            Files.createDirectories(destinationPath.getParent());

            // Sao chép file với tùy chọn ghi đè nếu file đã tồn tại
            Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File copied successfully to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            // Ghi lỗi vào log nếu có sự cố khi sao chép
            logger.severe("Failed to copy file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
