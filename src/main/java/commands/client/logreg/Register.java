package commands.client.logreg;

import commands.client.ClientCommand;
import datapacks.ResponsePackage;
import serverlogic.DBManipulation;

import java.io.Serial;

public class Register implements ClientCommand {
	@Override
	public ResponsePackage run(String username, String password, Object args) {
		username = username.trim();
		if (username.isEmpty() || username.contains(" ")) return new ResponsePackage(
				true,
				"username cannot be blank or contain spaces",
				null
		);
		if (DBManipulation.addUser(username, password)) return new ResponsePackage(
				false,
				"Registration successful",
				null
		);
		return new ResponsePackage(
				true,
				"This username is already taken",
				null
		);
	}

	@Override
	public String explain() {
		return "";
	}

	@Serial
	private static final long serialVersionUID = 1145596896552118546L;
}
