package serverlogic;

import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;
import exceptions.WrongNumberOfArgumentsException;

import java.sql.*;
import java.util.ResourceBundle;

public class ServerMain {

    public static void main(String... args) throws SQLException, NumberOutOfBoundsException, WrongNumberOfArgumentsException, NullFieldException {
        Program program = new Program();
        program.start();
    }
}
