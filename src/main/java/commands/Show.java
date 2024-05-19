package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;

public class Show implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {
        return new ResponsePackage(
                false,
                movieCollection.toString()
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
