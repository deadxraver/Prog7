package parsers;

import exceptions.EmptyStringException;


public class StringParser {
    /**
     * Parses the input string by removing leading and trailing whitespaces,
     * and collapsing multiple consecutive whitespaces into a single space.
     *
     * @param  line  the input string to be parsed
     * @return       the parsed string
     */
    public static String parse(String line) throws EmptyStringException {
        line = line.trim();
        if (line.isEmpty()) throw new EmptyStringException();
        while (line.contains("  ")) line = line.replaceAll(" {2}", " ");
        return line;
    }
}
