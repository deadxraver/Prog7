package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.EmptyCollectionException;

import java.io.Serial;
import java.io.Serializable;

public class MaxByMpaaRating implements NoMovieCommand, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        try {
            return new ResponsePackage(
                    false,
                    movieCollection.getMpaaMax().toString(),
                    null
            );
        } catch (EmptyCollectionException e) {
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
                max_by_mpaa_rating : prints element having max mpaa rating field
                max_by_mpaa_rating
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -1737272390454927855L;
}
