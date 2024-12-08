package storage.temporary;

import java.util.*;

public class TemporaryState {
    private Deque<String> remainingItems;

    TemporaryState() {
        this.remainingItems = new ArrayDeque<>();
    }

    public Deque<String> getRemainingItems() {
        return remainingItems;
    }

    public void setRemainingItems(Deque<String> remainingItems) {
        this.remainingItems = remainingItems;
    }
}
