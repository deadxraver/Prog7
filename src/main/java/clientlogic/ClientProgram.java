package clientlogic;

import annotations.MainMethod;
import commands.MovieGenerator;
import commands.movie.*;
import commands.nomovie.*;
import datapacks.RequestPackage;
import datapacks.ResponsePackage;
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


public class ClientProgram {
    private final int port;
    private final String host;
    private Scanner reader;
    private final SocketChannel socketChannel;
    private String username;
    private String password;
    private final HashMap<String, NoMovieCommand> commandHashMap = new HashMap<>();
    private final HashMap<String, ComesWithAMovie> movieCommandHashMap = new HashMap<>();
    private int fileCallsCount = 0;

    public ClientProgram(String[] args) throws WrongNumberOfArgumentsException, IOException {
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

    public ClientProgram() throws IOException {
        reader = new Scanner(System.in);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("connection_details_en");
        this.port = Integer.parseInt(resourceBundle.getString("port"));
        this.host = resourceBundle.getString("host");
        this.socketChannel = SocketChannel.open();
    }

    /**
     * Runs the program, which connects to a server, logs in or registers the user,
     * and then enters a command loop where the user can input commands to interact
     * with the server.
     *
     * @throws IOException if there is an error connecting to the server
     */
    @MainMethod
    public void run() throws IOException {
        System.out.println("Welcome! Type 'log' if you want to log in, type 'reg' if you have no account yet.");
//        socketChannel.connect(new InetSocketAddress(this.host, this.port));
        try (Socket socket = new Socket(host, port)) {
            while (true) {
                RequestPackage<?> authorizationPackage = this.getAuthorizationPackage();
                ResponsePackage responsePackage = authorize(authorizationPackage, socket);
                if (responsePackage.errorsOccurred()) {
                    System.err.println(responsePackage.message());
                    continue;
                }
                System.out.println(responsePackage.message());
                break;
            }

            fillHashMaps();

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
                    ResponsePackage responsePackage = sendAndGetResponse(socket, requestPackage);
                    if (responsePackage == null) System.err.println("Failed to get response package");
                    else if (responsePackage.errorsOccurred()) System.err.println(responsePackage.message());
                    else System.out.println(responsePackage.message());
                }
            } catch (NoSuchElementException ignored) {}
        }
    }

    /**
     * Retrieves the authorization package from the user input.
     *
     * @return the authorization package containing the login information
     */
    private RequestPackage<Boolean> getAuthorizationPackage() {
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

            System.out.println("password:");
            password = new String(System.console().readPassword()).trim();
            if (password.isEmpty()) {
                System.err.println("Password cannot be blank");
            } else break;

        }
        return new RequestPackage<>(
                username,
                password,
                null,
                null,
                isRegistered
        );
    }

    /**
     * Sends a request package and retrieves a response package.
     *
     * @param requestPackage the request package to be sent
     * @return the response package received
     */
    private ResponsePackage sendAndGetResponse(Socket socket, RequestPackage<?> requestPackage) {
        try (
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            objectOutputStream.writeObject(requestPackage);
            objectOutputStream.flush();
            return (ResponsePackage) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private RequestPackage<?> processCommand(String[] input) throws WrongNumberOfArgumentsException, NullFieldException, NumberFormatException, NoSuchCommandException {
        switch (input[0]) {
            case "add", "add_if_max", "remove_lower" -> {
                if (input.length == 1) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        null,
                        MovieGenerator.generateMovie(reader)
                );
                if (input.length == 2 || input.length == 3) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        input[1].equals("-a") ? null : input[1].equals("-m") ? username : input.length == 3 && input[1].equals("-u") ? input[2] : null,
                        MovieGenerator.generateMovie(reader)
                );
                else throw new WrongNumberOfArgumentsException();
            }
            case "help", "info", "print_field_ascending_operator", "show" -> {
                if (input.length == 1) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        "-a",
                        null
                );
                else throw new WrongNumberOfArgumentsException();
            }
            case "remove_all_by_oscars_count", "remove_by_id" -> {
                if (input.length == 2) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        input[1],
                        LongParser.parse(input[1])
                );
                else throw new WrongNumberOfArgumentsException();
            }
            case "clear", "remove_head" -> {
                if (input.length == 1) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        "-m",
                        null
                );
                if (input.length == 2 && (input[1].equals("-a") || input[1].equals("-m"))) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        input[1],
                        null
                );
                if (input.length == 3 && (input[1].equals("-a") || input[1].equals("-m"))) return new RequestPackage<>(
                        username,
                        password,
                        commandHashMap.get(input[0]),
                        input[1] + " " + input[2],
                        null
                );
                throw new WrongNumberOfArgumentsException();
            }
            default -> {
                throw new NoSuchCommandException();
            }
        }
    }

    private ResponsePackage authorize(RequestPackage<?> authorizationPackage, Socket socket) {
        try (
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            objectOutputStream.writeObject(authorizationPackage);
            objectOutputStream.flush();
            return (ResponsePackage) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return new ResponsePackage(
                    true,
                    e.getMessage(),
                    null
            );
        }
    }

    /**
     * Initializes the hash maps for movie commands and general commands.
     * Populates the movieCommandHashMap with instances of Add, AddIfMax, RemoveLower, and Update.
     * Populates the commandHashMap with instances of Clear, Help, Info, MaxByMpaaRating, PrintFieldAscendingOperator, RemoveHead, Show, RemoveAllByOscarsCount, and RemoveById.
     */
    private void fillHashMaps() {
        movieCommandHashMap.put("add", new Add());
        movieCommandHashMap.put("add_if_max", new AddIfMax());
        movieCommandHashMap.put("remove_lower", new RemoveLower());
        movieCommandHashMap.put("update", new Update());

        commandHashMap.put("clear", new Clear()); // todo varargs!!
        commandHashMap.put("help", new Help(commandHashMap, movieCommandHashMap));
        commandHashMap.put("info", new Info());
        commandHashMap.put("max_by_mpaa_rating", new MaxByMpaaRating());
        commandHashMap.put("print_field_ascending_operator", new PrintFieldAscendingOperator());
        commandHashMap.put("remove_head", new RemoveHead()); // todo varargs!!
        commandHashMap.put("show", new Show());

        commandHashMap.put("remove_all_by_oscars_count", new RemoveAllByOscarsCount());
        commandHashMap.put("remove_by_id", new RemoveById());
    }
}
