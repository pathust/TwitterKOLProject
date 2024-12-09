package storage.temporary;

import java.util.*;

public class TemporaryState {
    private List<String> remainingItems;

    TemporaryState() {
        this.remainingItems = new ArrayList<>();
    }

    public List<String> getRemainingItems() {
        return remainingItems;
    }

    public void setRemainingItems(List<String> remainingItems) {
        this.remainingItems = remainingItems;
    }
}
