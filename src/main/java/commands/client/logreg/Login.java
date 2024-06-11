package commands.client.logreg;

import commands.client.ClientCommand;
import datapacks.ResponsePackage;
import elements.User;
import serverlogic.DBManipulation;

public class Login implements ClientCommand {
	@Override
	public ResponsePackage run(String username, String password, Object args) {
		if (DBManipulation.getUser(username, password) != null) {
			logger.info("user {} successfully logged in", username);
			return new ResponsePackage(
					false,
					"Login successful",
					null
			);
		}
		else return new ResponsePackage(
				true,
				"Failed to log in, check your login and password",
				null
		);
	}

	@Override
	public String explain() {
		return "";
	}
}
