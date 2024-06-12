package commands.client;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;

public class Show implements ClientCommand {
	private static MovieCollection movieCollection;

	public Show(MovieCollection movieCollection) {
		Show.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		return new ResponsePackage(
				false,
				movieCollection,
				null
				);
	}

	@Override
	public String explain() {
		return """
				<==>
				show - prints the collection
				show
				<==>
				""";
	}
}
