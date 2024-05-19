package commands.servercommands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

public class Exit implements ServerCommand, Explainable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        System.exit(0);
        return null;
    }

    @Override
    public String explain() {
        return """
                <==>
                exit : Stops the server with exit status 0
                exit
                <==>
                """;
    }
}
