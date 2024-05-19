package parsers;

import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;

public class DoubleParser {
    public static final int UPPER_BOUND = 274;
    /**
     * Parses the input string and returns a Double value, while handling specific exceptions.
     *
     * @param  line  the input string to be parsed
     * @return       the parsed Double value
     */
    public static Double parse(String line) throws NullFieldException, NumberOutOfBoundsException {
        Double n = (Double)(double)(float)FloatParser.parse(line);
        if (n > UPPER_BOUND) throw new NumberOutOfBoundsException();
        return n;
    }
}
