package commands;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;

public class RemoveLower implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {
        // todo change logic
        if (movieCollection.removeLower((Movie) object)) {
            return new ResponsePackage(
                    false,
                    "All lower movies successfully deleted"
            );
        } else {
            return new ResponsePackage(
                    true,
                    "No lower movies found"
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
}
