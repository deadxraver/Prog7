package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import exceptions.EmptyCollectionException;

import java.io.Serial;
import java.io.Serializable;

public class MaxByMpaaRating implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {
        try {
            return new ResponsePackage(
                    false,
                    movieCollection.getMpaaMax().toString()
            );
        } catch (EmptyCollectionException e) {
            return new ResponsePackage(
                    true,
                    e.getMessage()
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
