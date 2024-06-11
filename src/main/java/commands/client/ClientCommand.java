package commands.client;

import commands.Command;
import datapacks.ResponsePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public interface ClientCommand extends Command, Serializable {
	Logger logger = LoggerFactory.getLogger(ClientCommand.class);
	ResponsePackage run(String username, String password, Object args);
}
