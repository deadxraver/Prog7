package serverlogic;

import commands.*;
import commands.servercommands.*;
import commands.servercommands.Help;
import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;
import exceptions.WrongNumberOfArgumentsException;
import parsers.IntParser;

import java.util.HashMap;
import java.util.ResourceBundle;

public class Program {
    public Program(String[] args) throws NumberOutOfBoundsException, NullFieldException, WrongNumberOfArgumentsException {
        if (args.length == 0) {
            this.host = connectionDetailsEn.getString("host");
            this.port = IntParser.parse(connectionDetailsEn.getString("port"));
        } else if (args.length == 1) {
            this.host = connectionDetailsEn.getString("host");
            this.port = IntParser.parse(args[0]);
        } else if (args.length == 2) {
            this.host = args[0];
            this.port = IntParser.parse(args[1]);
        } else throw new WrongNumberOfArgumentsException();
    }

    public Program() throws NumberOutOfBoundsException, NullFieldException {
        this.host = connectionDetailsEn.getString("host");
        this.port = IntParser.parse(connectionDetailsEn.getString("port"));
    }

    private final String host;
    private final int port;
    private final ResourceBundle databaseEn = ResourceBundle.getBundle("database_en");
    private final ResourceBundle connectionDetailsEn = ResourceBundle.getBundle("connection_details_en");
    private final HashMap<String, Command> commandHashMap = new HashMap<>();
    private final HashMap<String, ServerCommand> serverCommandHashMap = new HashMap<>();

    public void start() {

    }

    private void fillHashMaps() {
        commandHashMap.put("add", new Add());
        commandHashMap.put("add_if_max", new AddIfMax());
        commandHashMap.put("clear", new Clear());
        commandHashMap.put("help", new commands.Help(commandHashMap));
        commandHashMap.put("info", new Info());
        commandHashMap.put("max_by_mpaa_rating", new MaxByMpaaRating());
        commandHashMap.put("print_field_ascending_operator", new PrintFieldAscendingOperator());
        commandHashMap.put("remove_all_by_oscars_count", new RemoveAllByOscarsCount());
        commandHashMap.put("remove_by_id", new RemoveById());
        commandHashMap.put("remove_head", new RemoveHead());
        commandHashMap.put("remove_lower", new RemoveLower());
        commandHashMap.put("show", new Show());
        commandHashMap.put("update", new Update());

        serverCommandHashMap.put("exit", new Exit());
        serverCommandHashMap.put("grant", new Grant());
        serverCommandHashMap.put("help", new commands.servercommands.Help(serverCommandHashMap));
    }
}
