package commands.movie;

import commands.Command;
import elements.User;
import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;

public interface ComesWithAMovie extends Command {
    ResponsePackage addAMovie(MovieCollection movieCollection, User user, Movie movie);
}
