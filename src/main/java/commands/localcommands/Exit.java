package commands.localcommands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

public class Exit implements LocalCommand {

    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        System.exit(0);
        return null;
    }

    @Override
    public String explain() {
        return ""; // todo think of how to implement this
    }
}
