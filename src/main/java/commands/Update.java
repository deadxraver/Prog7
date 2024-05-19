package commands;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import exceptions.NoSuchMovieException;

import java.io.Serial;
import java.io.Serializable;

public class Update implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {
        // todo edit logic
        Movie movie = (Movie) object;
        try {
            movieCollection.getElement(movie.getId());
            return new ResponsePackage(
                    true,
                    "This id is already taken"
            );
        } catch (NoSuchMovieException e) {
            movieCollection.addMovie(movie);
            return new ResponsePackage(
                    false,
                    "Movie successfully updated"
            );
        }
    }

    @Override
    public String explain() {
        return """
                update : update fields of an existing movie
                update {id}
                {id}    : movie id to be updated
                """;
    }

    @Serial
    private static final long serialVersionUID = 4755656231949117218L;
}
