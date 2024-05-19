package parsers;

import elements.MovieGenre;
import exceptions.NoSuchGenreException;
import exceptions.NullFieldException;

public class GenreParser {
    /**
     * Parses the given string to a MovieGenre.
     *
     * @param  line  the string to be parsed
     * @return      the parsed MovieGenre
     */
    public static MovieGenre parse(String line) throws NullFieldException, NoSuchGenreException {
        line = line.toLowerCase().replaceAll(" ", "");
        if (line.isEmpty()) throw new NullFieldException();
        MovieGenre genre = line.equals("drama") ? MovieGenre.DRAMA
                : line.equals("fantasy") ? MovieGenre.FANTASY
                : line.equals("tragedy") ? MovieGenre.TRAGEDY
                : line.equals("western") ? MovieGenre.WESTERN
                : null;
        if (genre == null) throw new NoSuchGenreException();
        return genre;
    }
}
