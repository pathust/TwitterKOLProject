package storage.temporary;

import java.util.*;

public class TemporaryState {
    int currentIndex;
    int lastSavedIndex;
    private List<String> itemUniqueKeys;

    public int getLastSavedIndex() {
        return lastSavedIndex;
    }

    public void setLastSavedIndex(int lastSavedIndex) {
        this.lastSavedIndex = lastSavedIndex;
    }

    TemporaryState() {
        this.itemUniqueKeys = new ArrayList<>();
        currentIndex = 0;
        lastSavedIndex = 0;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public List<String> getItemUniqueKeys() {
        return itemUniqueKeys;
    }

    public void setItemUniqueKeys(List<String> itemUniqueKeys) {
        this.itemUniqueKeys = itemUniqueKeys;
    }

    public boolean isExist(String key) {
        return itemUniqueKeys.contains(key);
    }

    public String getCurrentItemUniqueKey() {
        return itemUniqueKeys.get(currentIndex);
    }

    public void addItemUniqueKey(String key) {
        System.out.println("size before added in TemporaryState: " + itemUniqueKeys.size());
        this.itemUniqueKeys.add(key);
        System.out.println("size after added in TemporaryState: " + itemUniqueKeys.size());
    }

}
