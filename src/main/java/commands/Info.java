package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;

public class Info implements Command, Serializable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        // TODO
        return new ResponsePackage(
                false,
                null // TODO replace
        );
    }

    @Override
    public String explain() {
        return """
                info : prints info about user and collection
                info
                """;
    }

    @Serial
    private static final long serialVersionUID = 8741645253024385335L;
}
