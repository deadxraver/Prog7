package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

import java.io.Serial;
import java.io.Serializable;

public class Info implements NoMovieCommand, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object arg) {
        String info = user.toString() + '\n' +
                "Stored elements type: Movie\n" +
                "Number of movies stored: " + movieCollection.getNumberOfMovies() + '\n' +
                "Number of movies stored for current user: " + movieCollection.getNumberOfMovies(user) + '\n';
        return new ResponsePackage(
                false,
                info,
                null
        );
    }

    @Override
    public String explain() {
        return """
                info : prints info about user and collection
                info
                """;
    }

    @Serial
    private static final long serialVersionUID = 8741645253024385335L;
}
