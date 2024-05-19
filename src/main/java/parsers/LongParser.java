package parsers;

import exceptions.NullFieldException;

public class LongParser {
    /**
     * Parses a string to a long, removing underscores if present.
     *
     * @param  line  the string to be parsed
     * @return       the parsed long value
     */
    public static long parse(String line) throws NumberFormatException, NullFieldException {
        if (line.isEmpty()) throw new NullFieldException();
        line = line.replaceAll("_", "");
        return Long.parseLong(line);
    }
}
