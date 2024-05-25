package commands.movie;

import elements.User;
import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;

public class RemoveLower implements ComesWithAMovie, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        if (movieCollection.removeLower((Movie) object, user)) {
            return new ResponsePackage(
                    false,
                    "All lower movies successfully deleted",
                    null
            );
        } else {
            return new ResponsePackage(
                    true,
                    "No lower movies found",
                    null
            );
        }
    }

    @Override
    public String explain() {
        return """
                <==>
                remove_lower : removes all the elements lower than entered
                remove_lower [ -a | -m | -u {username} ]
                !Flags below are available only for superuser!
                -a              : apply for all users
                -m              : apply only for current user (default)
                -u {username}   : apply for entered user
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -8205772871816724728L;

    @Override
    public ResponsePackage addAMovie(MovieCollection movieCollection, User user, Movie movie) {
        return run(movieCollection, user, movie);
    }
}
