package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

import java.io.Serial;
import java.io.Serializable;

public class RemoveAllByOscarsCount implements NoMovieCommand, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        if (movieCollection.removeByOscar((Integer) object, user)) {
            return new ResponsePackage(
                    false,
                    "Movies with such oscars count have been successfully deleted",
                    null
            );
        } else {
            return new ResponsePackage(
                    true,
                    "No matches found",
                    null
            );
        }
    }

    @Override
    public String explain() {
        return """
                <==>
                remove_all_by_oscars_count : removes all movies having entered number of oscars
                remove_all_by_oscars_count [ -a | -m | -u {username} ] {oscars_count}
                {oscars_count}  : oscars_count field
                !Flags below are available only for superuser!
                -a              : apply for all users
                -m              : apply only for current user (default)
                -u {username}   : apply for entered user
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -3928714350596327042L;
}
