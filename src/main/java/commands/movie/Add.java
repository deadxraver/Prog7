package commands.movie;

import elements.User;
import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;

public class Add implements ComesWithAMovie, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        movieCollection.addMovie((Movie) object);
        return new ResponsePackage(
                false,
                "Movie successfully added",
                null
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                add : asks user to fill in the information about movie and then adds it to collection
                add
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -9203005047180764168L;

    @Override
    public ResponsePackage addAMovie(MovieCollection movieCollection, User user, Movie movie) {
        return run(movieCollection, user, movie);
    }
}
