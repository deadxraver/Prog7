package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

import java.io.Serial;
import java.io.Serializable;


public class Clear implements NoMovieCommand, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object arg) {
        movieCollection.clear(user);
        return new ResponsePackage(
                false,
                (user != null ? ("Collection for user " + user) : "Collection for all users") + " successfully cleared",
                null
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                clear : clears the collection
                clear [ -a | -m | -u {username} ]
                !Flags below are available only for superuser!
                -a              : apply for all users
                -m              : apply only for current user (default)
                -u {username}   : apply for entered user
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -717981351376024031L;
}
