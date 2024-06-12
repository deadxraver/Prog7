package commands.client;

import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.AccessException;
import exceptions.ParseException;
import parsers.ArgsParser;
import serverlogic.DBManipulation;

import java.sql.SQLException;

public class Clear implements ClientCommand {
	private static MovieCollection movieCollection;

	public Clear(MovieCollection movieCollection) {
		Clear.movieCollection = movieCollection;
	}

	@Override
	public ResponsePackage run(String username, String password, Object args) {
		User user = DBManipulation.getUser(username, password);
		try {
			String flagsAndAll = (String) args;
			User applyFor = ArgsParser.parse(user, flagsAndAll);
			movieCollection.clear(applyFor);
			return new ResponsePackage(
					false,
					"collection for user" + (applyFor == null ? "s " : (" " + applyFor.getUsername() + " ")) + "successfully cleared",
					null
			);
		} catch (ClassCastException e) {
			return new ResponsePackage(
					true,
					"Failed to cast arguments",
					null
			);
		} catch (AccessException | ParseException | SQLException e) {
			return new ResponsePackage(
					true,
					e.getMessage(),
					null
			);
		}
	}

	@Override
	public String explain() {
		return """
				<==>
				clear - clears collection
				clear [ -u { user } | -a | -m ]
				-u { user }  : apply for typed user (only available or superuser)
				-a           : apply for all (only available for superuser)
				-m           : apply for self (default)
				<==>
				""";
	}
}
