package storage.temporary;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TemporaryStorage{
    private TemporaryState temporaryState;

    public TemporaryStorage() {
        this.temporaryState = new TemporaryState();
    }

    public TemporaryState getTemporaryState() {
        return temporaryState;
    }

    public void setTemporaryState(TemporaryState temporaryState) {
        this.temporaryState = temporaryState;
    }

    public void add(String key){
        if (!temporaryState.isExist(key)) {
            temporaryState.addItemUniqueKey(key);
        }
    }

    public void save(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int i = 0;
            writer.write("Processed:" + temporaryState.getLastSavedIndex());
            writer.newLine();
            for (String key : temporaryState.getItemUniqueKeys()) {
                String tag = (i++ < temporaryState.getLastSavedIndex()) ? "SAVED" : "UNSAVED";
                writer.write(tag + ":" + key);
                writer.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        temporaryState.setLastSavedIndex(temporaryState.getCurrentIndex());
    }

    public void load(String filePath) throws IOException {
        TemporaryState state = new TemporaryState();
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            if ((line = reader.readLine()) != null && line.startsWith("Processed:")) {
                int lastSavedIndex = Integer.parseInt(line.split(":")[1]);
                state.setLastSavedIndex(lastSavedIndex);
                state.setCurrentIndex(lastSavedIndex);
            }

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("SAVED") || line.startsWith("UNSAVED")) {
                    String key = line.split(":", 2)[1];
                    state.addItemUniqueKey(key);
                }
            }
        }

        temporaryState = state;
    }

    public void clearTemporaryStorage(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public void setProcessed() {
        temporaryState.setCurrentIndex(temporaryState.getCurrentIndex() + 1);
    }

    public List<String> getUnprocessedItemUniqueKeys() {
        List<String> itemUniqueKeys = new ArrayList<>();
        List<String> keys = temporaryState.getItemUniqueKeys();
        for (int i = temporaryState.getCurrentIndex(); i < keys.size(); i++)
            itemUniqueKeys.add(keys.get(i));
        return itemUniqueKeys;
    }
}
