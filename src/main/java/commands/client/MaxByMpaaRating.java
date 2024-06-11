package commands.client;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.EmptyCollectionException;

public class MaxByMpaaRating implements ClientCommand {
	private static MovieCollection movieCollection;

	public MaxByMpaaRating(MovieCollection movieCollection) {
		MaxByMpaaRating.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		try {
			return new ResponsePackage(
					false,
					movieCollection.getMpaaMax().toString(),
					null
			);
		} catch (EmptyCollectionException e) {
			return new ResponsePackage(
					true,
					"Collection is empty",
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				max_by_mpaa_rating - prints the movie with the greatest mpaa rating
				max_by_mpaa_rating
				<==>
				""";
	}
}
