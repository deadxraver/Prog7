package parsers;

import exceptions.DateIsNotReachedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateTimeParser {
    /**
     * Parses the given string to create a LocalDateTime object.
     *
     * @param  line  the string to be parsed
     * @return       the parsed LocalDateTime object
     */
    public static LocalDateTime parse(String line) throws DateTimeParseException, DateIsNotReachedException {
        if (line.trim().isEmpty()) return null;
        line += "T00:00:00.0000000";
        LocalDateTime localDateTime = LocalDateTime.parse(line);
        if (localDateTime.isAfter(LocalDateTime.now())) {
            throw new DateIsNotReachedException();
        }
        return localDateTime;
    }
}
