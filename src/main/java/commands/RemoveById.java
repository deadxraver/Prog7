package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import exceptions.NoSuchMovieException;

import java.io.Serial;
import java.io.Serializable;

public class RemoveById implements Command, Serializable {

    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {
        try {
            movieCollection.removeMovie((Long) object);
            return new ResponsePackage(
                    false,
                    null
            );
        } catch (NoSuchMovieException e) {
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
                remove_by_id : removes a movie with entered id
                remove_by_id {id}
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = 6947888240733986366L;
}
