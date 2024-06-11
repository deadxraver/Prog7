package commands.client;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import elements.User;
import exceptions.EmptyCollectionException;
import serverlogic.DBManipulation;

import java.sql.SQLException;

public class AddIfMax implements ClientCommand {

	private static MovieCollection movieCollection;

	public AddIfMax(MovieCollection movieCollection) {
		AddIfMax.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		Movie movie;
		User user = DBManipulation.getUser(username, password);
		try {
			movie = (Movie) args;
		} catch (ClassCastException e) {
			return new ResponsePackage(
					true,
					"Failed to cast object",
					null
			);
		}
		try {
			Movie maxMovie = movieCollection.getMax();
			if (maxMovie.getOscarsCount() > movie.getOscarsCount()) return new ResponsePackage(
					true,
					"Movie is not max",
					null
			);
		} catch (EmptyCollectionException ignored) {}
		try {
			movieCollection.addMovie(user, movie);
			return new ResponsePackage(
					false,
					"Movie successfully added",
					null
			);
		} catch (SQLException e) {
			return new ResponsePackage(
					true,
					"failed to add movie",
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				add_if_max - adds a new movie if it is greater than every other
				add_if_max
				<==>
				""";
	}
}
