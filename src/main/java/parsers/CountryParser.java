package parsers;

import elements.Country;
import exceptions.NoSuchCountryException;

public class CountryParser {
    /**
     * Parses the input line to determine the corresponding Country enum value.
     *
     * @param  line  the input line to be parsed
     * @return      the corresponding Country enum value, or null if not found
     */
    public static Country parse(String line) throws NoSuchCountryException {
        line = line.toLowerCase().replaceAll(" ", "");
        if (line.isEmpty()) return null;
        Country country = line.equals("china") ? Country.CHINA
                : line.equals("germany") ? Country.GERMANY
                : line.equals("japan") ? Country.JAPAN
                : line.equals("spain") ? Country.SPAIN
                : line.equals("vatican") ? Country.VATICAN
                : null;
        if (country == null) throw new NoSuchCountryException();
        return country;
    }
}
