package serverlogic;

import annotations.MainMethod;
import commands.movie.*;
import commands.nomovie.*;
import commands.servercommands.*;
import datapacks.RequestPackage;
import datapacks.ResponsePackage;
import elements.MovieCollection;
import elements.User;
import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;
import exceptions.WrongNumberOfArgumentsException;
import multithread.TaskHandler;
import parsers.IntParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ServerProgram {
    {
        commandHashMap = new HashMap<>();
        movieCommandHashMap = new HashMap<>();
        serverCommandHashMap = new HashMap<>();
        fillHashMaps();
    }

    public ServerProgram(int serverNumber, String... args) throws NumberOutOfBoundsException, NullFieldException, WrongNumberOfArgumentsException, SQLException {
        if (args.length == 0) {
            this.host = connectionDetailsEn.getString("host");
            this.port = IntParser.parse(connectionDetailsEn.getString("port1"));
        } else if (args.length == 1) {
            this.host = connectionDetailsEn.getString("host");
            this.port = IntParser.parse(args[0]);
        } else if (args.length == 2) {
            this.host = args[0];
            this.port = IntParser.parse(args[1]);
        } else throw new WrongNumberOfArgumentsException();
        this.serverNumber = serverNumber;
        connection = DriverManager.getConnection(dbUrl, dbLogin, dbPassword);
    }

    private final ResourceBundle databaseEn = ResourceBundle.getBundle("database_en");
    private final ResourceBundle connectionDetailsEn = ResourceBundle.getBundle("connection_details_en");
    private final String dbUrl = databaseEn.getString("url");
    private final String dbLogin = databaseEn.getString("login");
    private final String dbPassword = databaseEn.getString("password");
    private final String host;
    private int port;
    private static Connection connection;
    private static HashMap<String, NoMovieCommand> commandHashMap;
    private static HashMap<String, ComesWithAMovie> movieCommandHashMap;
    private static HashMap<String, ServerCommand> serverCommandHashMap;
    private static final Logger logger = LoggerFactory.getLogger(ServerProgram.class.getSimpleName());
    private final int serverNumber;
    private ServerSocketChannel serverSocketChannel;
    private static final MovieCollection movieCollection = new MovieCollection(LocalDate.now()); // todo с бд подтягивать
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private SocketChannel socketChannel;


    @MainMethod
    public static void startServer(int numberOfThreads) throws NumberOutOfBoundsException, WrongNumberOfArgumentsException, SQLException, NullFieldException {
        if (numberOfThreads < 1 || numberOfThreads > 8) throw new WrongNumberOfArgumentsException();
        for (int i = 0; i < numberOfThreads; i++) {
            ServerProgram serverProgram = new ServerProgram(i);
            TaskHandler.addTask(serverProgram::run);
        }
        TaskHandler.start();
    }

    public static void startServer() throws NumberOutOfBoundsException, WrongNumberOfArgumentsException, SQLException, NullFieldException {
        startServer(1);
    }

    public void run() {
        logger.info("Server #{} started in thread {}", serverNumber, Thread.currentThread().getName());
        try {
            establishConnection();
            logger.info("Connection established, current address is: {}", serverSocketChannel.getLocalAddress());
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
            System.exit(1);
        }
        while (true) {
            if (connect()) {
                try (
                        Socket socket = socketChannel.socket();
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
                ) {
                    logger.info("Connection accepted, waiting for request...");
                    RequestPackage<?> requestPackage = (RequestPackage<?>) objectInputStream.readObject();
                    logger.info("request received, {}", requestPackage);
                    if (requestPackage.command() == null) {
                        objectOutputStream.writeObject(new ResponsePackage(
                                false,
                                "Cool! You are" + new User(requestPackage.username(), requestPackage.password()),
                                null
                        ));
                        objectOutputStream.flush();
                        logger.info("Response sent");
                    } else {
                        // todo
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("{}", e.getMessage());
                }
            } try {
                if (reader.ready()) {
                    String inp = reader.readLine().trim();
                    ServerCommand serverCommand = serverCommandHashMap.get(inp);
                    if (serverCommand == null) continue;
                    if (serverCommand.equals(serverCommandHashMap.get("exit")) || serverCommand.equals(serverCommandHashMap.get("help")))
                        serverCommand.run(
                                movieCollection,
                                null,
                                null
                        );
                    else {
                        serverCommand.run(
                                movieCollection,
                                null, // todo (get user from db)
                                null
                        );
                    }
                }
            } catch (IOException e) {
                logger.error("{}", e.getMessage());
            }
        }
    }

    private boolean connect() {
        try {
            if ((socketChannel = serverSocketChannel.accept()) == null) throw new IOException();
            logger.info("Client connected");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private RequestPackage<?> getRequest(Socket socket) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            return (RequestPackage<?>) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private void sendRequest(Socket socket, ResponsePackage responsePackage) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            objectOutputStream.writeObject(responsePackage);
            objectOutputStream.flush();
        }
    }

//    public static void spinLoop() {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//            ServerCommand serverCommand = null;
//            try {
//                serverCommand = serverCommandHashMap.get(bufferedReader.readLine().trim());
//            } catch (IOException ignored) {
//            }
//            buffer = null;
//            if (serverCommand == null) logger.error("Unknown command");
//            else serverCommand.run(movieCollection, null, null);
//    }

    private synchronized void establishConnection() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
//        for (int i = 0; i < 4; i++) {
//            try {
//                if (i == 0) serverSocketChannel.bind(new InetSocketAddress(host, port));
//                else {
//                    serverSocketChannel.bind(new InetSocketAddress(host, Integer.parseInt(connectionDetailsEn.getString("port" + i))));
//                }
//            } catch (Exception e) {
//                continue;
//            }
//            break;
//        }
        serverSocketChannel.bind(new InetSocketAddress(1841));
        serverSocketChannel.configureBlocking(false);
    }

    private void fillHashMaps() {
        movieCommandHashMap.put("add", new Add());
        movieCommandHashMap.put("add_if_max", new AddIfMax());
        movieCommandHashMap.put("remove_lower", new RemoveLower());
        movieCommandHashMap.put("update", new Update());

        commandHashMap.put("clear", new Clear());
        commandHashMap.put("help", new commands.nomovie.Help(commandHashMap, movieCommandHashMap));
        commandHashMap.put("info", new Info());
        commandHashMap.put("max_by_mpaa_rating", new MaxByMpaaRating());
        commandHashMap.put("print_field_ascending_operator", new PrintFieldAscendingOperator());
        commandHashMap.put("remove_all_by_oscars_count", new RemoveAllByOscarsCount());
        commandHashMap.put("remove_by_id", new RemoveById());
        commandHashMap.put("remove_head", new RemoveHead());
        commandHashMap.put("show", new Show());

        serverCommandHashMap.put("exit", new Exit());
        serverCommandHashMap.put("grant", new Grant());
        serverCommandHashMap.put("help", new commands.servercommands.Help(serverCommandHashMap));
    }
}
