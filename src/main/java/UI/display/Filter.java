package UI.display;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Tweet;
import model.User;

public class Filter {

    public FilteredList<User> applyKOLFilter(TextField searchField, ObservableList<User> masterData, TableView<User> tableView) {
        FilteredList<User> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                        user.getProfileLink().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

        return filteredData;
    }

    public FilteredList<Tweet> applyTweetFilter(TextField searchField, ObservableList<Tweet> masterData, TableView<Tweet> tableView) {
        // Create a FilteredList for Tweet data
        FilteredList<Tweet> filteredData = new FilteredList<>(masterData, p -> true);

        // Add a listener to filter data when the search text changes
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tweet -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Filtering logic based on Tweet attributes
                return tweet.getAuthorUsername().toLowerCase().contains(lowerCaseFilter) ||
                        tweet.getAuthorProfileLink().toLowerCase().contains(lowerCaseFilter) ||
                        tweet.getContent() != null && tweet.getContent().toLowerCase().contains(lowerCaseFilter) ||
                        tweet.getTweetLink().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Wrap the filtered data in a SortedList for sorting
        SortedList<Tweet> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Set the TableView items to the sorted and filtered data
        tableView.setItems(sortedData);

        return filteredData;
    }
}
