package storage.temporary;

import java.util.*;

public class TemporaryState<T> {
    private List<T> completedItems;
    private Deque<T> remainingItems;

    TemporaryState() {
        this.completedItems = new ArrayList<>();
        this.remainingItems = new ArrayDeque<>();
    }

    public List<T> getCompletedItems() {
        return completedItems;
    }

    public void setCompletedItems(List<T> completedItems) {
        this.completedItems = completedItems;
    }

    public Deque<T> getRemainingItems() {
        return remainingItems;
    }

    public void setRemainingItems(Deque<T> remainingItems) {
        this.remainingItems = remainingItems;
    }
}
