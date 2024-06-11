package commands.server;

public class Help implements ServerCommand {
	@Override
	public String run(String args) {
		return """
				<==>
				help - get info about available commands
				help
				<==>
				<==>
				grant - grant/revoke superuser rights
				grant { username }
				<==>
				<==>
				exit - shut down the program
				exit
				<==>
				""";
	}

	@Override
	public String explain() {
		return "";
	}
}
