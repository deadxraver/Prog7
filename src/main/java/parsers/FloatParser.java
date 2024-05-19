package parsers;

import exceptions.NullFieldException;

public class FloatParser {
    /**
     * Parses a string to a Float after replacing spaces, underscores, and commas with dots.
     *
     * @param  line  the string to be parsed
     * @return      the parsed Float value
     */
    public static Float parse(String line) throws NumberFormatException, NullFieldException {
        line = line.replaceAll(" ", "").replaceAll("_", "").replaceAll(",", ".");
        if (line.isEmpty()) throw new NullFieldException();
        return Float.parseFloat(line);
    }
}
