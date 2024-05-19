package commands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Help implements Command, Serializable {
    public Help(HashMap<String, Command> commandHashMap1) {
        commandHashMap = commandHashMap1;
    }

    private static HashMap<String, Command> commandHashMap;

    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        StringBuilder sb = new StringBuilder();
        for (Command command : commandHashMap.values()) {
            sb.append(command.explain()).append('\n');
        }
        return new ResponsePackage(
                false,
                sb.toString()
        );
    }

    @Override
    public String explain() {
        return """
                <==>
                help : prints info about available commands
                help
                <==>
                """;
    }

    @Serial
    private static final long serialVersionUID = -3450999954951132884L;
}
