package commands;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import exceptions.EmptyCollectionException;

import java.io.Serial;
import java.io.Serializable;

public class AddIfMax implements Command, Serializable {

    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object object) {
        try {
            if (((Movie)object).getCoordinates().getLength() >
                    movieCollection.getMax().getCoordinates().getLength()) {
                movieCollection.addMovie((Movie) object);
                return new ResponsePackage(
                        false,
                        "Movie successfully added"
                );
            }
        } catch (EmptyCollectionException ignored) {}
        return new ResponsePackage(
                true,
                "Movie is not max"
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                add_if_max : asks user to fill in the information about movie and adds it into the collection if it is the greatest
                add_if_max
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = 2874280002097979299L;
}
