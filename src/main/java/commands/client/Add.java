package commands.client;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import elements.User;
import serverlogic.DBManipulation;

import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;

public class Add implements ClientCommand, Serializable {
	private static MovieCollection movieCollection;

	public Add(MovieCollection movieCollection) {
		Add.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		Movie movie;
		try {
			movie = (Movie) args;
		} catch (ClassCastException e) {
			return new ResponsePackage(
					true,
					"Could not cast object",
					null
			);
		}
		try {
			movieCollection.addMovie(DBManipulation.getUser(username, password), movie);
			return new ResponsePackage(
					false,
					"Movie successfully added",
					null
			);
		} catch (SQLException e) {
			return new ResponsePackage(
					true,
					"Could not add movie",
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				add - adds a new movie to collection
				add
				<==>
				""";
	}
}
