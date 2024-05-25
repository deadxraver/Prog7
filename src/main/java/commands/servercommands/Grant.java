package commands.servercommands;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

public class Grant implements ServerCommand {
    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object arg) {

        // TODO grant or take superuser
        return new ResponsePackage(
                false,
                null, // todo specify granted or revoked
                null
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
