package commands.client;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.AccessException;
import exceptions.NoSuchMovieException;
import exceptions.NullFieldException;
import parsers.LongParser;
import serverlogic.DBManipulation;

public class RemoveById implements ClientCommand {
	private static MovieCollection movieCollection;

	public RemoveById(MovieCollection movieCollection) {
		RemoveById.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		User user = DBManipulation.getUser(username, password);
		try {
			movieCollection.removeMovie(LongParser.parse((String)args), user);
			return new ResponsePackage(
					false,
					"movie successfully removed",
					null
			);
		} catch (NullFieldException | NoSuchMovieException | AccessException e) {
			return new ResponsePackage(
					true,
					e.getMessage(),
					null
			);
		} catch (NumberFormatException e) {
			return new ResponsePackage(
					true,
					"Wrong number format",
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				remove_by_id - removes a movie having entered id
				remove_by_id { id }
				<==>
				""";
	}
}
