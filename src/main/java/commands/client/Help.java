package commands.client;

import commands.Command;
import datapacks.ResponsePackage;
import elements.User;

import java.util.HashMap;

public class Help implements ClientCommand {
	private final HashMap<String, ClientCommand> clientCommandHashMap;

	public Help(HashMap<String, ClientCommand> clientCommandHashMap) {
		this.clientCommandHashMap = clientCommandHashMap;
	}
	@Override
	public ResponsePackage run(String username, String password, Object args) {
		StringBuilder stringBuilder = new StringBuilder();
		for (ClientCommand clientCommand : clientCommandHashMap.values()) {
			stringBuilder.append(clientCommand.explain());
		}
		return new ResponsePackage(
				false,
				stringBuilder.toString(),
				null
		);
	}

	@Override
	public String explain() {
		return """
				<==>
				help - prints info about available commands
				help
				<==>
				""";
	}
}
