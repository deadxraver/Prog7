package parsers;

import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;

public class IntParser {
    public static final int LOWER_OSCAR_BOUND = 0;
    /**
     * Parses the given string to an integer after removing underscores.
     *
     * @param  line  the string to be parsed
     * @return       the parsed integer value
     */
    public static int parse(String line) throws NumberFormatException, NullFieldException, NumberOutOfBoundsException {
        if (line.isEmpty()) throw new NullFieldException();
        line = line.replaceAll("_", "");
        int n = Integer.parseInt(line);
        if (n <= LOWER_OSCAR_BOUND) throw new NumberOutOfBoundsException();
        return n;
    }
}
