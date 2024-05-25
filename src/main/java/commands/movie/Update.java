package commands.movie;

import elements.User;
import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import exceptions.AccessException;
import exceptions.NoSuchMovieException;
import exceptions.SecurityException;

import java.io.Serial;
import java.io.Serializable;

public class Update implements ComesWithAMovie, Serializable {
    /**
     * Runs the command with the given movie collection and object.
     *
     * @param movieCollection the movie collection to update
     * @param object          the object containing the movie id
     * @return a response package containing the result of the update
     */
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        Long id = (Long) object;
        Movie movie;
        try {
            movie = movieCollection.getElement(id);
        } catch (NoSuchMovieException e) {
            return new ResponsePackage(
                    true,
                    "No movie with such id",
                    null
            );
        }
        if (movie.belongsTo(user)) {
            return new ResponsePackage(
                    false,
                    "Waiting for a new movie...",
                    movie
            );
        }
        return new ResponsePackage(
                true,
                "Movie does not belong to this user",
                null
        );
    }

    /**
     * Returns a string explaining how to update the fields of an existing movie.
     *
     * @return a string explaining the update command syntax
     */
    @Override
    public String explain() {
        return """
                update : update fields of an existing movie
                update {id}
                {id}    : movie id to be updated
                """;
    }

    /**
     * Updates a movie in the movie collection by removing the existing movie with the same ID and adding the new movie.
     *
     * @param movieCollection the movie collection to update
     * @param movie           the new movie to add
     * @return a response package indicating the success or failure of the update
     */
    @Override
    public ResponsePackage addAMovie(MovieCollection movieCollection, User user, Movie movie) {
        try {
            movieCollection.removeMovie(movie, user);
            movieCollection.addMovie(movie);
        } catch (AccessException e) {
            return new ResponsePackage(
                    true,
                    new SecurityException().getMessage(),
                    null
            );
        }
        return new ResponsePackage(
                false,
                "Movie successfully updated",
                null
        );
    }

    @Serial
    private static final long serialVersionUID = 4755656231949117218L;
}
