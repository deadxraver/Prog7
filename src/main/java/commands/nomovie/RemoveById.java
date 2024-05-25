package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.AccessException;
import exceptions.NoSuchMovieException;

import java.io.Serial;
import java.io.Serializable;

public class RemoveById implements NoMovieCommand, Serializable {

    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        try {
            movieCollection.removeMovie((Long) object, user);
            return new ResponsePackage(
                    false,
                    "Movie successfully deleted",
                    null
            );
        } catch (NoSuchMovieException | AccessException e) {
            return new ResponsePackage(
                    true,
                    e.getMessage(),
                    null
            );
        }
    }

    @Override
    public String explain() {
        return """
                <==>
                remove_by_id : removes a movie with entered id
                remove_by_id {id}
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = 6947888240733986366L;
}
