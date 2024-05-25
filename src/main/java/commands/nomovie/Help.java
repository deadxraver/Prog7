package commands.nomovie;

import commands.Command;
import commands.movie.ComesWithAMovie;
import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Help implements NoMovieCommand, Serializable {
    public Help(HashMap<String, NoMovieCommand> commandHashMap1, HashMap<String, ComesWithAMovie> movieCommandHashMap1) {
        commandHashMap = commandHashMap1;
        movieCommandHashMap = movieCommandHashMap1;
    }

    private static HashMap<String, NoMovieCommand> commandHashMap;
    private static HashMap<String, ComesWithAMovie> movieCommandHashMap;

    @Override
    public ResponsePackage run(MovieCollection movieCollection, User user, Object arg) {
        StringBuilder sb = new StringBuilder();
        for (NoMovieCommand command : commandHashMap.values()) {
            sb.append(command.explain()).append('\n');
        }
        for (Command command : movieCommandHashMap.values()) {
            sb.append(command.explain()).append('\n');
        }
        return new ResponsePackage(
                false,
                sb.toString(),
                null
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                help : prints info about available commands
                help
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -3450999954951132884L;
}
