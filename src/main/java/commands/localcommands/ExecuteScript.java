package commands.localcommands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

public class ExecuteScript implements LocalCommand {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        return null; // todo decide whether to leave the concept or not
    }

    @Override
    public String explain() {
        return "";
    }
}
