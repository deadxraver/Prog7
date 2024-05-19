package parsers;

import elements.Color;
import exceptions.NoSuchColorException;

public class ColorParser {
    /**
     * Parses the given input string and returns the corresponding Color value.
     *
     * @param  line  the input string to be parsed
     * @return       the Color value corresponding to the input string, or null if no match is found
     */
    public static Color parse(String line) throws NoSuchColorException {
        line = line.toLowerCase().replaceAll(" ", "");
        if (line.isEmpty()) return null;
        Color color = line.equals("green") ? Color.GREEN
                : line.equals("orange") ? Color.ORANGE
                : line.equals("white") ? Color.WHITE
                : null;
        if (color == null) throw new NoSuchColorException();
        return color;
    }
}
