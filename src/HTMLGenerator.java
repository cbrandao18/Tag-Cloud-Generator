import static java.text.MessageFormat.format;

import java.io.PrintWriter;
import java.util.Queue;

/**
 * HTML generator class.
 *
 * @author Mihai Petrescu and Christie Brandao
 */
public final class HTMLGenerator {
    /**
     * HTML document header template.
     */
    static final String HEADER = "<html><head><title>Top {0} words in {1}</title>"
            + "<link href=\"http://web.cse.ohio-state.edu/software/2231/"
            + "web-sw2/assignments/projects/tag-cloud-generator/data/"
            + "tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">"
            + "</head><body><h2>Top {0} words in {1}</h2>"
            + "<hr><div class=\"cdiv\"><p class=\"cbox\">";
    /**
     * HTML document entry template.
     */
    static final String ENTRY = "<span style=\"cursor:default\" class=\"f{2}\" "
            + "title=\"count: {1}\">{0}</span> ";
    /**
     * HTML document footer.
     */
    static final String FOOTER = "</p></div></body></html>";
    /**
     * Output range to map to.
     */
    static final Range RANGE_OUT = new Range(11, 48);

    /**
     * You done complaining, eclipse?
     */
    private HTMLGenerator() {
    }

    /**
     * Internal Range class.
     *
     * @author Mihai Petrescu and Christie Brandao
     */
    private static class Range {
        /**
         * Minimum and maximum values.
         */
        public int min, max;

        /**
         * Default constructor.
         *
         * @param min
         *            Minimum value
         * @param max
         *            Maximum value
         * @ensures this.min = min and this.max = max
         */
        Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Converts {@code this} to its string representation.
         *
         * @ensures toString = this.min * "->" * this.max
         */
        @Override
        public String toString() {
            return this.min + " -> " + this.max;
        }
    }

    /**
     * Gets the maximum and minimum value in the values of the map given.
     *
     * @param words
     *            input words
     * @param wc
     *            word counter object
     * @ensures getRange = a Range of the maximum and minimum values present in
     *          the counts of {@code words}
     */
    private static Range getRange(Queue<String> words, WordCount wc) {
        Range r = new Range(Integer.MAX_VALUE, 0);
        for (String s : words) {
            int c = wc.get(s);
            if (c < r.min) {
                r.min = c;
            } else if (c > r.max) {
                r.max = c;
            }
        }
        return r;
    }

    /**
     * Map value in range to another range. Shamelessly stolen from Arduino.
     *
     * @param x
     *            value to map
     * @param ri
     *            input range
     * @param ro
     *            output range
     * @ensures map = x mapped from {@code ri} to {@code ro}
     */
    private static int map(int x, Range ri, Range ro) {
        int ret;
        if (ri.max == ri.min) {
            ret = (ro.max + ro.min) / 2;
        } else {
            ret = (x - ri.min) * (ro.max - ro.min) / (ri.max - ri.min) + ro.min;
        }
        return ret;
    }

    /**
     * Write the word count to the HTML file.
     *
     * @param wc
     *            word count
     * @param count
     *            count of words to add to file
     * @param filename
     *            input filename
     * @param out
     *            output file
     * @ensures out = an html page containing a word cloud of the top
     *          {@code count} words
     */
    public static void writeHTML(WordCount wc, int count, String filename,
            PrintWriter out) {
        out.print(format(HEADER, count, filename));
        Queue<String> top = wc.getTop(count);
        Range rangeIn = getRange(top, wc);
        while (!top.isEmpty()) {
            String word = top.poll();
            int c = wc.get(word);
            int size = map(c, rangeIn, RANGE_OUT);
            out.print(format(ENTRY, word, c, size));
        }
        out.print(FOOTER);
    }
}
