package parsers;

import elements.User;
import exceptions.AccessException;
import exceptions.ParseException;
import serverlogic.DBManipulation;

public class ArgsParser {
	private ArgsParser() {
	}

	/**
	 * Parses the arguments of the commands applied to targets <p>
	 * {@code -u {user}} - if applied to particular user <p>
	 * {@code -a} - if applied to everyone <p>
	 * {@code -m} - if applied to self (default)
	 *
	 * @param args input arguments after command
	 * @return user, to whom the command will be applied, null if to everyone
	 */
	public static User parse(User user, String args) throws AccessException, ParseException {
		if (args == null) return user;
		args = args.trim();
		if (args.isEmpty() || args.equals("-m")) return user;
		if (args.equals("-a")) {
			if (user.isSuperuser()) return null;
			throw new AccessException();
		}
		if (args.split(" ")[0].equals("-u") && args.split(" ").length == 2) {
			User toBeApplied = DBManipulation.get(args.split(" ")[1], user);
			if (toBeApplied == null) throw new AccessException();
			return toBeApplied;
		} else throw new ParseException();
	}
}