package serverlogic;

import commands.server.Grant;
import commands.server.Help;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConsole {
	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	protected static BufferedReader reader;
	private static final Logger logger = LoggerFactory.getLogger(ServerConsole.class);

	private ServerConsole() {
	}

	public static void readConsole() {
		try {
			if (!reader.ready()) throw new IOException();
			String input = reader.readLine().trim();
			if (input.isEmpty()) throw new IOException();
			if (input.equals("exit")) {
				logger.info("shutting down...");
				System.exit(0);
			} else if (input.equals("help")) {
				System.out.println(new Help().run(null));
			} else if (input.split(" ")[0].equals("grant")) {
				if (input.split(" ").length != 2) {
					logger.error("1 argument expected, got {} instead", input.split(" ").length - 1);
					return;
				}
				new Grant().run(input.split(" ")[1]);
			} else {
				System.err.println("No such command");
			}
		} catch (IOException ignored) {
		}
	}
}
