package commands.nomovie;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

import java.io.Serial;
import java.io.Serializable;

public class Show implements NoMovieCommand, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object object) {
        return new ResponsePackage(
                false,
                movieCollection.toString(),
                null
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                show : prints the whole collection
                show
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -2051979921933526308L;
}
