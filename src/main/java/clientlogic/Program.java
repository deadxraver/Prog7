package clientlogic;

import commands.*;
import commands.localcommands.MovieGenerator;
import datapacks.AuthorizationPackage;
import datapacks.RequestPackage;
import datapacks.ResponsePackage;
import elements.Movie;
import exceptions.NoSuchCommandException;
import exceptions.NullFieldException;
import exceptions.WrongNumberOfArgumentsException;
import parsers.LongParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;

import static sun.security.util.Password.readPassword;

public class Program {
    private final int port;
    private final String host;
    private Scanner reader;
    private final SocketChannel socketChannel;
    private String username;
    private String password;
    private final HashMap<String, Command> commandHashMap = new HashMap<>();
    private int fileCallsCount = 0;

    public Program(String[] args) throws WrongNumberOfArgumentsException, IOException {
        reader = new Scanner(System.in);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("connection_details_en");
        if (args.length == 0) {
            this.port = Integer.parseInt(resourceBundle.getString("port"));
            this.host = resourceBundle.getString("host");
        } else if (args.length == 1) {
            this.port = Integer.parseInt(args[0]);
            this.host = resourceBundle.getString("host");
        } else if (args.length == 2) {
            this.port = Integer.parseInt(args[1]);
            this.host = args[0];
        } else throw new WrongNumberOfArgumentsException();
        this.socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
    }

    public Program() throws IOException {
        reader = new Scanner(System.in);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("connection_details_en");
        this.port = Integer.parseInt(resourceBundle.getString("port"));
        this.host = resourceBundle.getString("host");
        this.socketChannel = SocketChannel.open();
    }

    public void run() throws IOException {
        System.out.println("Welcome! Type 'log' if you want to log in, type 'reg' if you have no account yet.");
        socketChannel.connect(new InetSocketAddress(this.host, this.port));
        while (true) {
            AuthorizationPackage authorizationPackage = this.getAuthorizationPackage();
            ResponsePackage responsePackage = authorize(authorizationPackage);
            if (responsePackage.errorsOccurred()) {
                System.err.println(responsePackage.message());
                continue;
            }
            System.out.println(responsePackage.message());
            break;
        }
        fillHashMap();

        try {
            while (true) {
                if (fileCallsCount >= 100 || !reader.hasNext()) {
                    reader = new Scanner(System.in);
                    fileCallsCount = 0;
                }
                System.out.print("[" + username + "]=# ");
                String[] input = reader.nextLine().trim().split(" ");
                if (input.length == 0) continue;
                if (input[0].equals("exit")) {
                    if (input.length != 1) {
                        System.err.println("this command does not take any arguments");
                        continue;
                    } else {
                        System.exit(0);
                    }
                }
                if (input[0].equals("execute_script")) {
                    if (input.length != 2) {
                        System.err.println("this command takes exactly one argument");
                    } else {
                        reader = new Scanner(new FileReader(input[1]));
                        fileCallsCount++;
                    }
                    continue;
                }
                RequestPackage<?> requestPackage;
                try {
                    requestPackage = processCommand(input);
                } catch (WrongNumberOfArgumentsException | NumberFormatException | NullFieldException |
                         NoSuchCommandException e) {
                    System.err.println(e.getMessage());
                    continue;
                }
                ResponsePackage responsePackage = sendAndGetResponse(requestPackage);
                // todo
            }
        } catch (NoSuchElementException ignored) {
        }
    }

    private AuthorizationPackage getAuthorizationPackage() {
        boolean isRegistered;
        while (true) {
            String temp = reader.nextLine().trim();
            if (!temp.equals("log") && !temp.equals("reg")) continue;
            isRegistered = temp.equals("log");
            break;
        }
        while (true) {
            System.out.println("login:");
            username = reader.nextLine().trim();
            if (username.isEmpty()) {
                System.err.println("Username cannot be blank");
            } else break;
        }
        while (true) {
            try {
                System.out.println("password:");
                password = new String(readPassword(System.in)).trim();
                if (password.isEmpty()) {
                    System.err.println("Password cannot be blank");
                } else break;
            } catch (IOException ignored) {
            }
        }
        return new AuthorizationPackage(
                isRegistered,
                username,
                password
        );
    }

    private ResponsePackage sendAndGetResponse(RequestPackage<?> requestPackage) {

    }

    private RequestPackage<?> processCommand(String[] input) throws WrongNumberOfArgumentsException, NullFieldException, NumberFormatException, NoSuchCommandException {
        switch (input[0]) {
            case "add", "add_if_max", "remove_lower" -> {
                if (input.length == 1) return new RequestPackage<Movie>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        MovieGenerator.generateMovie(0, reader)
                );
                else throw new WrongNumberOfArgumentsException();
            }
            case "help", "info", "print_field_ascending_operator", "show",
                 "update" -> { // todo think of update implementation
                if (input.length == 1) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        null
                );
                else throw new WrongNumberOfArgumentsException();
            }
            case "remove_all_by_oscars_count", "remove_by_id" -> {
                if (input.length == 2) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        LongParser.parse(input[1])
                );
                else throw new WrongNumberOfArgumentsException();
            }
            case "clear" -> {
                // todo clear with flags
                return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        null // todo replace with varargs
                );
            }
            case "remove_head" -> {
                // todo remove_head with flags
                return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        null // todo replace with varargs
                );
            }
            default -> {
                throw new NoSuchCommandException();
            }
        }
    }

    private ResponsePackage authorize(AuthorizationPackage authorizationPackage) {
        try (
                Socket socket = socketChannel.socket();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            objectOutputStream.writeObject(authorizationPackage);
            objectOutputStream.flush();
            return (ResponsePackage) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException ignored) {
        }
        return new ResponsePackage(
                true,
                "Failed to get response"
        );
    }

    private void fillHashMap() {
        commandHashMap.put("add", new Add());
        commandHashMap.put("add_if_max", new AddIfMax());
        commandHashMap.put("remove_lower", new RemoveLower());

        commandHashMap.put("clear", new Clear()); // todo varargs!!
        commandHashMap.put("help", new Help(commandHashMap));
        commandHashMap.put("info", new Info());
        commandHashMap.put("max_by_mpaa_rating", new MaxByMpaaRating());
        commandHashMap.put("print_field_ascending_operator", new PrintFieldAscendingOperator());
        commandHashMap.put("remove_head", new RemoveHead()); // todo varargs!!
        commandHashMap.put("show", new Show());

        commandHashMap.put("remove_all_by_oscars_count", new RemoveAllByOscarsCount());
        commandHashMap.put("remove_by_id", new RemoveById());
        commandHashMap.put("update", new Update());
    }
}
