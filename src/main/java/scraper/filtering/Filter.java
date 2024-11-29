package scraper.filtering;

import java.util.List;

public interface Filter {
    void advancedSearch(List<String> words, int minLikes, int minReplies, int minReposts);
}
