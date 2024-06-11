package commands.client;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import elements.User;
import serverlogic.DBManipulation;

import java.sql.SQLException;

public class RemoveLower implements ClientCommand {
	private static MovieCollection movieCollection;
	public RemoveLower(MovieCollection movieCollection) {
		RemoveLower.movieCollection = movieCollection;
	}
	@Override
	public ResponsePackage run(String username, String password, Object args) {
		User user = DBManipulation.getUser(username, password);
		try {
			Movie movie = (Movie) args;
			if (!movieCollection.removeLower(movie, user)) throw new SQLException();
			return new ResponsePackage(
					false,
					"Movies successfully deleted",
					null
			);
		} catch (ClassCastException e) {
			return new ResponsePackage(
					true,
					"Failed to cast object",
					null
			);
		} catch (SQLException e) {
			return new ResponsePackage(
					true,
					"Failed to remove objects",
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				remove_lower - removes all movies lower than current
				remove_lower
				<==>
				""";
	}
}
