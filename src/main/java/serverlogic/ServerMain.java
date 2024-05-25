package serverlogic;

import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;
import exceptions.WrongNumberOfArgumentsException;
import multithread.TaskHandler;

import java.sql.SQLException;

public class ServerMain {
    public static void main(String... args) throws NumberOutOfBoundsException, WrongNumberOfArgumentsException, NullFieldException, SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}
        ServerProgram.startServer(3);
    }

}
