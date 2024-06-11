package commands.client;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.AccessException;
import exceptions.EmptyCollectionException;
import exceptions.ParseException;
import parsers.ArgsParser;
import serverlogic.DBManipulation;

public class RemoveHead implements ClientCommand {
	private static MovieCollection movieCollection;

	public RemoveHead(MovieCollection movieCollection) {
		RemoveHead.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		User user = DBManipulation.getUser(username, password);
		try {
			User applyFor = ArgsParser.parse(user, (String) args);
			movieCollection.removeMovie(movieCollection.getMax(), applyFor);
			return new ResponsePackage(
					false,
					"movie successfully removed",
					null
			);
		} catch (EmptyCollectionException | AccessException e) {
			return new ResponsePackage(
					true,
					e.getMessage(),
					null
			);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				remove_head - remove the greatest element
				remove_head [ -u { user } | -a | -m ]
				-u { user } : applies for entered user (for superuser only)
				-a          : applies for all users (for superuser only)
				-m          : applies for sender (default)
				<==>
				""";
	}
}
