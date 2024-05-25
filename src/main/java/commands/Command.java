package commands;

import elements.User;
import datapacks.ResponsePackage;
import elements.MovieCollection;

public interface Command {
    /**
     * Runs the command with the given MovieCollection and argument.
     *
     * @param movieCollection the MovieCollection object representing the movies
     * @param arg             the argument for the command
     * @return the ResponsePackage object containing the result of the command
     */
    ResponsePackage run(MovieCollection movieCollection, User user, Object arg);

    /**
     * Returns a string explaining how to use this command.
     *
     * @return a string explaining how to use this command
     */
    String explain();

}
