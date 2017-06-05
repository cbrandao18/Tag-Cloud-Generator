import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

/**
 * Main word counting class.
 *
 * @author Mihai Petrescu and Christie Brandao
 *
 */
public final class WordCount {
    /**
     * Map of words to their counts.
     */
    private Map<String, Integer> wordMap = new HashMap<>();

    /**
     * String builder for input text.
     */
    private StringBuilder input = new StringBuilder();
    /**
     * Sorted word set.
     */
    private TreeSet<String> sortedWords = new TreeSet<>(new Comparator<String>() {

        @Override
        public int compare(String o1, String o2) {
            int i1 = WordCount.this.wordMap.get(o1), i2 = WordCount.this.wordMap.get(o2);
            if (i2 - i1 == 0) {
                return o1.compareTo(o2);
            } else {
                return i2 - i1;
            }
        }
    });

    /**
     * Count the words in this object.
     *
     * @requires this.input is not empty
     * @ensures this = a map of all the words and their values
     */
    public void count() {
        String text = this.input.toString();

        for (String word : text.split("[^a-z]+")) {
            if (this.wordMap.containsKey(word)) {
                int count = this.wordMap.get(word) + 1;
                this.wordMap.replace(word, count);
            } else {
                this.wordMap.put(word, 1);
            }
        }

        this.sortKeys();
    }

    /**
     * Sorts the words in the set by their count in descending order.
     */
    private void sortKeys() {
        for (String key : this.wordMap.keySet()) {
            this.sortedWords.add(key);
        }
    }

    /**
     * Get the first {@code count} words by word count.
     *
     * @param count
     *            Amount of words to get
     * @requires count <= |this|
     * @ensures getTop = alphabetically sorted queue of the top {@code count}
     *          words
     */
    public Queue<String> getTop(int count) {
        Queue<String> top = new PriorityQueue<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < count; i++) {
            top.add(this.sortedWords.pollFirst());
        }

        return top;
    }

    /**
     * Get {@code word}'s word count.
     *
     * @param word
     *            Word to find.
     * @ensures get = the umber of occurrences of {@code word} in text
     */
    public int get(String word) {
        return this.wordMap.get(word);
    }

    /**
     * Add text to the buffer.
     *
     * @param text
     *            The text to add.
     * @ensures this.input = #this.input * text
     */
    public void add(String text) {
        this.input.append(text.toLowerCase());
        this.input.append(" ");
    }

}
