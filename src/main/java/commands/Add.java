package commands;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;

public class Add implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {

        movieCollection.addMovie((Movie) object);
        return new ResponsePackage(
                false,
                "Movie successfully added"
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                add : asks user to fill in the information about movie and then adds it to collection
                add
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -9203005047180764168L;
}
