package commands.client;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;
import parsers.IntParser;
import serverlogic.DBManipulation;

import java.sql.SQLException;

public class RemoveAllByOscarsCount implements ClientCommand {
	private static MovieCollection movieCollection;

	public RemoveAllByOscarsCount(MovieCollection movieCollection) {
		RemoveAllByOscarsCount.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		User user = DBManipulation.getUser(username, password);
		try {
			if (!movieCollection.removeByOscar(IntParser.parse((String) args), user)) throw new SQLException();
			return new ResponsePackage(
					false,
					"Movies having " + args + " successfully deleted",
					null
			);
		} catch (ClassCastException e) {
			return new ResponsePackage(
					true,
					"Failed to cast object",
					null
			);
		} catch (NumberFormatException e) {
			return new ResponsePackage(
					true,
					"Failed to parse number",
					null
			);
		} catch (NumberOutOfBoundsException | NullFieldException e) {
			return new ResponsePackage(
					true,
					e.getMessage(),
					null
			);
		} catch (SQLException e) {
			return new ResponsePackage(
					true,
					"Failed to remove movies",
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				remove_all_by_oscars_count - removes elements having entered number of oscars
				remove_all_by_oscars_count { number }
				<==>
				""";
	}
}
