package commands.servercommands;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

public class Exit implements ServerCommand {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object arg) {
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
