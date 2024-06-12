package commands.client;

import datapacks.ResponsePackage;
import elements.Movie;
import elements.MovieCollection;
import elements.User;
import exceptions.AccessException;
import exceptions.EmptyCollectionException;
import exceptions.ParseException;
import parsers.ArgsParser;
import serverlogic.DBManipulation;

import java.util.Arrays;

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
			Object[] movies = movieCollection.getCollection();
			Arrays.sort(movies);
			for (int i = movies.length - 1; i >= 0; i--) {
				Movie movie = (Movie) movies[i];
				if (applyFor == null || movie.belongsTo(applyFor)) {
					movieCollection.removeMovie(movie, applyFor);
					return new ResponsePackage(
							false,
							"movie successfully removed",
							null
					);
				}
			}
			return new ResponsePackage(
					true,
					"No suitable movies found",
					null
			);
		} catch (AccessException e) {
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
