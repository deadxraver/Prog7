package commands.servercommands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

public class Grant implements ServerCommand, Explainable {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        String username = (String) arg;
        // TODO grant or take superuser
        return new ResponsePackage(
                false,
                null // todo specify granted or revoked
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                grant : grants or revokes superuser privileges for selected user
                grant {username}
                <==>
                """;
    }
}
