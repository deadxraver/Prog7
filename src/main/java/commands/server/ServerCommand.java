package commands.server;

import commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ServerCommand extends Command {

	Logger logger = LoggerFactory.getLogger(ServerCommand.class);
	String run(String args);
}
