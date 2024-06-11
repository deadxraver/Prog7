package clientlogic;


import commands.MovieGenerator;
import commands.client.*;
import commands.client.logreg.Disconnect;
import commands.client.logreg.Login;
import commands.client.logreg.Register;
import datapacks.RequestPackage;
import datapacks.ResponsePackage;
import elements.Movie;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ClientProgram {
	private Scanner reader;
	private final HashMap<String, ClientCommand> clientCommandHashMap;
	private SocketChannel socketChannel;
	private int fileCallsCount = 0;
	private String username;
	private String password;
	private final ResourceBundle resourceBundle;
	private Socket socket;

	public ClientProgram() {
		reader = new Scanner(System.in);
		clientCommandHashMap = new HashMap<>();
		this.resourceBundle = ResourceBundle.getBundle("connection_details_en");
		fillHashMap();
		for (int i = 0; true; i++) {
			try {
				socketChannel = SocketChannel.open();
				socketChannel.connect(new InetSocketAddress(resourceBundle.getString("host"), Integer.parseInt(resourceBundle.getString("port"))));
				break;
			} catch (IOException e) {
				System.err.println("Server is unavailable right now, try again later");
			}
			if (i == 3) System.exit(1);
			System.out.println("Retrying in 5 seconds");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ignored) {
			}
		}
	}

	public void run() {
		try {
			socket = socketChannel.socket();
			login();

			while (true) {
				System.out.print("=# ");
				if (!reader.hasNext() || fileCallsCount > 100) {
					reader = new Scanner(System.in);
					fileCallsCount = 0;
				}
				String[] input = reader.nextLine().trim().split(" ");
				if (input.length == 0) continue;
				if (checkLocal(input)) continue;
				ClientCommand clientCommand = clientCommandHashMap.get(input[0]);
				if (clientCommand == null) {
					System.err.println("No such command");
					continue;
				}
				RequestPackage requestPackage = generateRequest(input, clientCommand);
				ResponsePackage responsePackage;
				try {
					if (!send(requestPackage)) continue;
					responsePackage = receive();
					process(responsePackage);
				} catch (IOException | ClassNotFoundException e) {
					System.err.println(e.getMessage());
					continue;
				}
				if (responsePackage.movie() != null) {
					Movie movie = MovieGenerator.generateMovie(responsePackage.movie(), reader);
					try (
							Socket socket = socketChannel.socket()
							) {
						if (!send(new RequestPackage(
								username,
								password,
								new Update(null),
								movie
						))) {
							System.err.println("Failed to send");
							continue;
						}
						responsePackage = receive();
						process(responsePackage);
					} catch (IOException | ClassNotFoundException e) {
						System.err.println(e.getMessage());
					}
				}
			}
		} catch (NoSuchElementException e) {
			System.out.println("Shutting down...");
		} finally {
			try (
					Socket socket = socketChannel.socket();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
					) {
				objectOutputStream.writeObject(new RequestPackage(
						username,
						password,
						new Disconnect(),
						null
				));
				objectOutputStream.flush();
				objectInputStream.readObject();
			} catch (IOException | ClassNotFoundException ignored) {
			}
		}
	}

	private void login() {
		RequestPackage requestPackage;
		while (true) {
			System.out.println("Enter 'log' if you want to login, 'reg' if you are a new user");
			String inp = reader.nextLine().trim();
			if (inp.equals("log")) {
				System.out.println("Login: ");
				username = reader.nextLine().trim();
				System.out.println("Password: ");
				password = new String(System.console().readPassword());
				requestPackage = new RequestPackage(
						username,
						password,
						new Login(),
						null
				);
			} else if (inp.equals("reg")) {
				System.out.println("Login: ");
				username = reader.nextLine().trim();
				System.out.println("Password: ");
				password = new String(System.console().readPassword());
				requestPackage = new RequestPackage(
						username,
						password,
						new Register(),
						new String[]{username, password}
				);
			} else continue;
			try {
				if (!send(requestPackage)) continue;
				ResponsePackage responsePackage = receive();
				process(responsePackage);
				if (!responsePackage.errorsOccurred()) {
					System.out.println("Successfully logged in");
					return;
				}
			} catch (IOException | ClassNotFoundException e) {
				System.err.println(e.getMessage());
			}

		}
	}

	private boolean send(RequestPackage requestPackage) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(requestPackage);
		objectOutputStream.flush();
		return true;
	}

	private ResponsePackage receive() throws IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
		return (ResponsePackage) objectInputStream.readObject();
	}

	private boolean checkLocal(String[] input) {
		if (input[0].equals("exit") && input.length == 1) System.exit(0);
		if (input[0].equals("exit")) {
			if (input.length == 2) {
				try {
					int code = Integer.parseInt(input[1]);
					System.exit(code);
				} catch (NumberFormatException e) {
					System.err.println("Argument should be an integer");
				}
			} else System.err.println("This command takes 1 or 0 arguments");
			return true;
		}
		if (input[0].equals("execute_script")) {
			if (input.length == 2) {
				try {
					reader = new Scanner(new FileReader(input[1]));
					fileCallsCount++;
				} catch (FileNotFoundException e) {
					System.err.println("No such file");
				}
			} else System.err.println("This command takes exactly one argument");
			return true;
		}
		return false;
	}

	private void process(ResponsePackage responsePackage) {
		if (responsePackage.errorsOccurred()) System.err.println(responsePackage.message());
		else System.out.println(responsePackage.message());
	}

	private boolean checkForMovie(String[] input) {
		switch (input[0]) {
			case "add", "add_if_max", "remove_lower" -> {
				return true;
			}
			default -> {
				return false;
			}
		}
	}

	private RequestPackage generateRequest(String[] input, ClientCommand command) {
		Object o;
		if (checkForMovie(input)) {
			o = MovieGenerator.generateMovie(reader);
		} else {
			if (input.length == 1) o = null;
			else {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < input.length; i++) {
					sb.append(input[i]).append(' ');
				}
				o = sb.toString().trim();
			}
		}
		return new RequestPackage(
				username,
				password,
				command,
				o
		);
	}

	private void fillHashMap() {
		clientCommandHashMap.put("add", new Add(null));
		clientCommandHashMap.put("add_if_max", new AddIfMax(null));
		clientCommandHashMap.put("clear", new Clear(null));
		clientCommandHashMap.put("help", new Help(clientCommandHashMap));
		clientCommandHashMap.put("info", new Info());
		clientCommandHashMap.put("max_by_mpaa_rating", new MaxByMpaaRating(null));
		clientCommandHashMap.put("print_field_ascending_operator", new PrintFieldAscendingOperator(null));
		clientCommandHashMap.put("remove_all_by_oscars_count", new RemoveAllByOscarsCount(null));
		clientCommandHashMap.put("remove_by_id", new RemoveById(null));
		clientCommandHashMap.put("remove_head", new RemoveHead(null));
		clientCommandHashMap.put("remove_lower", new RemoveLower(null));
		clientCommandHashMap.put("show", new Show(null));
		clientCommandHashMap.put("update", new Update(null));
	}
}