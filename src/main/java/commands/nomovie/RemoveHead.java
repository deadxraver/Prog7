package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.AccessException;
import exceptions.EmptyCollectionException;
import exceptions.NoSuchMovieException;

import java.io.Serial;
import java.io.Serializable;

public class RemoveHead implements NoMovieCommand, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) { // todo change logic
        try {
            movieCollection.removeMovie(movieCollection.getMax(), user);
        } catch (EmptyCollectionException | AccessException e) {
            return new ResponsePackage(
                    true,
                    e.getMessage(),
                    null
            );
        }
        return new ResponsePackage(
                false,
                "Movie successfully deleted",
                null
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                remove_head : removes the greatest movie out of collection
                remove_head [ -a | -m | -u {username} ]
                !Flags below are available only for superuser!
                -a              : apply for all users
                -m              : apply only for current user (default)
                -u {username}   : apply for entered user
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -925527377588774830L;
}
