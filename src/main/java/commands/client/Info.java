package commands.client;

import datapacks.ResponsePackage;
import elements.User;
import serverlogic.DBManipulation;

public class Info implements ClientCommand {
	@Override
	public ResponsePackage run(String username, String password, Object args) {
		User user = DBManipulation.getUser(username, password);
		if (user == null) return new ResponsePackage(
				true,
				"No such user",
				null
		);
		return new ResponsePackage(
				false,
				user.toString(),
				null
		);
	}

	@Override
	public String explain() {
		return """
				<==>
				info - prints info about user
				info
				<==>
				""";
	}
}
