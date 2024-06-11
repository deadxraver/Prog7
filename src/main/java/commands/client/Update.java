package commands.client;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import elements.User;
import exceptions.AccessException;
import exceptions.NoSuchMovieException;
import exceptions.NullFieldException;
import parsers.LongParser;
import serverlogic.DBManipulation;

import java.sql.SQLException;

public class Update implements ClientCommand {
	private static MovieCollection movieCollection;

	public Update(MovieCollection movieCollection) {
		Update.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		User user = DBManipulation.getUser(username, password);
		try {
			Movie movie = (Movie) args;
			movieCollection.removeMovie(movie.getId(), user);
			movieCollection.addMovie(user, movie);
			return new ResponsePackage(
					false,
					"Movie successfully updated",
					null
			);
		} catch (ClassCastException e) {
			try {
				long id = LongParser.parse((String) args);
				Movie movie = movieCollection.getElement(id, user);
				return new ResponsePackage(
						false,
						"",
						movie
				);
			} catch (NullFieldException | NoSuchMovieException | AccessException ex) {
				return new ResponsePackage(
						true,
						ex.getMessage(),
						null
				);
			} catch (ClassCastException ex) {
				return new ResponsePackage(
						true,
						"Failed to cast",
						null
				);
			}
		} catch (AccessException | NoSuchMovieException e) {
			return new ResponsePackage(
					true,
					e.getMessage(),
					null
			);
		} catch (SQLException e) {
			return new ResponsePackage(
					true,
					"Could not replace movie",
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				update - updates an existing movie
				update
				<==>
				""";
	}
}
