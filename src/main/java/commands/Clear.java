package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;


public class Clear implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        if (arg == null) {
            movieCollection.clear();
            return new ResponsePackage(
                    false,
                    "Collection successfully cleared"
            );
        }
        int userId = (Integer) arg;
        movieCollection.clear(userId);
        return new ResponsePackage(
                false,
                "Collection for user " + userId + " successfully cleared"
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
