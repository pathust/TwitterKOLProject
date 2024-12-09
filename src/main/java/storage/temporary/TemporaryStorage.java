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
        System.out.println("Size before added in TemporaryStorage: " + temporaryState.getItemUniqueKeys().size());
        this.temporaryState.addItemUniqueKey(key);
        System.out.println("Size after added in TemporaryStorage: " + temporaryState.getItemUniqueKeys().size());
    }

    public void save(String filePath) {
        System.out.println("Saving temporary state to " + filePath);
        System.out.println("Temporary state size is " + temporaryState.getItemUniqueKeys().size());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int i = 0;
            writer.write("Processed:" + temporaryState.getLastSavedIndex());
            writer.newLine();
            for (String key : temporaryState.getItemUniqueKeys()) {
                String tag = (i++ <= temporaryState.getLastSavedIndex()) ? "SAVED" : "UNSAVED";
                writer.write(tag + ": " + key);
                writer.newLine();
            }
            System.out.println("Saved " + temporaryState.getItemUniqueKeys().size() + " items to " + filePath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        temporaryState.setLastSavedIndex(temporaryState.getCurrentIndex());
    }

    public TemporaryState load(String filePath) throws IOException {
        TemporaryState state = new TemporaryState();
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return state;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            if ((line = reader.readLine()) != null && line.startsWith("Processed:")) {
                int lastSavedIndex = Integer.parseInt(line.split(":")[1]);
                state.setLastSavedIndex(lastSavedIndex);
                state.setCurrentIndex(lastSavedIndex);
            }

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("SAVED:") || line.startsWith("UNSAVED:")) {
                    String key = line.split(":", 2)[1];
                    state.addItemUniqueKey(key);
                }
            }
        }

        return state;
    }

    public void clearTemporaryStorage(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public void setProcessed() {
        this.temporaryState.setCurrentIndex(temporaryState.getCurrentIndex() + 1);
    }

    public List<String> getUnprocessedItemUniqueKeys() {
        List<String> itemUniqueKeys = temporaryState.getItemUniqueKeys();
        return itemUniqueKeys.subList(temporaryState.getCurrentIndex(), itemUniqueKeys.size() - 1);
    }
}
