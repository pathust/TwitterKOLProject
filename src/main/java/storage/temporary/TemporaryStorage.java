package storage.temporary;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TemporaryStorage{
    private TemporaryState temporaryState;

    public TemporaryStorage() {
        temporaryState = new TemporaryState();
    }

    public TemporaryState getTemporaryState() {
        return temporaryState;
    }

    public void setTemporaryState(TemporaryState temporaryState) {
        this.temporaryState = temporaryState;
    }

    public void add(String link){
        List<String> remainingItems = temporaryState.getRemainingItems();
        remainingItems.add(link);
        temporaryState.setRemainingItems(remainingItems);
    }

    public void save(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String remaining : temporaryState.getRemainingItems()) {
                writer.write(remaining);
                writer.newLine();
            }
        }
    }

    public TemporaryState load(String filePath) throws IOException {
        TemporaryState state = new TemporaryState();
        File file = new File(filePath);
        if (!file.exists()) {
            return state;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                state.getRemainingItems().add(line);
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

    public void remove(String link) {
        List<String> remainingItems = temporaryState.getRemainingItems();
        remainingItems.remove(link);
        temporaryState.setRemainingItems(remainingItems);
    }
}
