package commands.movie;

import elements.User;
import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import exceptions.EmptyCollectionException;

import java.io.Serial;
import java.io.Serializable;

public class AddIfMax implements ComesWithAMovie, Serializable {

    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        try {
            if (((Movie)object).getCoordinates().getLength() >
                    movieCollection.getMax().getCoordinates().getLength()) {
                movieCollection.addMovie((Movie) object);
                return new ResponsePackage(
                        false,
                        "Movie successfully added",
                        null
                );
            }
        } catch (EmptyCollectionException ignored) {}
        return new ResponsePackage(
                true,
                "Movie is not max",
                null
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                add_if_max : asks user to fill in the information about movie and adds it into the collection if it is the greatest
                add_if_max
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = 2874280002097979299L;

    @Override
    public ResponsePackage addAMovie(MovieCollection movieCollection, User user, Movie movie) {
        return run(movieCollection, user, movie);
    }
}
