package commands.servercommands;

import datapacks.ResponsePackage;
import elements.MovieCollection;

import java.util.HashMap;

public class Help implements ServerCommand {
    public Help(HashMap<String, ServerCommand> serverCommandHashMap1) {
        serverCommandHashMap = serverCommandHashMap1;
    }
    private static HashMap<String, ServerCommand> serverCommandHashMap;
    @Override
    public ResponsePackage run(MovieCollection movieCollection, Object arg) {
        StringBuilder sb = new StringBuilder();
        for (ServerCommand sc : serverCommandHashMap.values()) sb.append(sc.explain()).append('\n');
        return new ResponsePackage(
                false,
                sb.toString()
        );
    }

    @Override
    public String explain() {
        return """
                help : prints info about available commands
                help
                """;
    }
}
