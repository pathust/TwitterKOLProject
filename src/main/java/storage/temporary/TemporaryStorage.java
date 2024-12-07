package storage.temporary;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class TemporaryStorage <T> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void save(String filePath, TemporaryState<T> temporaryState) {
        try {
            objectMapper.writeValue(new File(filePath), temporaryState.getRemainingItems());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TemporaryState<T> load(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        TemporaryState<T> temporaryState = objectMapper.readValue(file, TemporaryState.class);
        return temporaryState;
    }

    public void clearTemporaryStorage(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
