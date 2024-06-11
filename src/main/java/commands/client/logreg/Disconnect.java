package commands.client.logreg;

import commands.client.ClientCommand;
import datapacks.ResponsePackage;

public class Disconnect implements ClientCommand {
	@Override
	public ResponsePackage run(String username, String password, Object args) {
		logger.info("user {} disconnected", username);
		return new ResponsePackage(
				false,
				null,
				null
		);
	}

	@Override
	public String explain() {
		return "";
	}
}
