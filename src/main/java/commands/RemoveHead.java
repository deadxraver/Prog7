package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import exceptions.EmptyCollectionException;
import exceptions.NoSuchMovieException;

import java.io.Serial;
import java.io.Serializable;

public class RemoveHead implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) { // todo change logic
        try {
            movieCollection.removeMovie(movieCollection.getMax().getId());
        } catch (EmptyCollectionException e) {
            return new ResponsePackage(
                    true,
                    e.getMessage()
            );
        } catch (NoSuchMovieException ignored) {
        }
        return new ResponsePackage(
                false,
                "Movie successfully deleted"
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
