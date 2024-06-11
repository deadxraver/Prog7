package commands.server;

import serverlogic.DBManipulation;

import java.sql.SQLException;

public class Grant implements ServerCommand {
	@Override
	public String run(String args) {
		String username = args.trim();
		try {
			logger.info("{} is {} a superuser", username, DBManipulation.grant(username) ? "now" : "no longer");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public String explain() {
		return "";
	}
}
