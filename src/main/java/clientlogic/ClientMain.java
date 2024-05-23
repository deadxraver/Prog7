package clientlogic;

import exceptions.WrongNumberOfArgumentsException;

import java.io.IOException;

public class ClientMain {
    public static void main(String... args) {
        try {
            new Program(args).run();
        } catch (WrongNumberOfArgumentsException | NumberFormatException e) {
            System.err.println(e.getMessage());
            System.err.print("Try: ");
            System.out.println("java -jar client.jar [ {port} | {host} {port} ]");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Make sure you entered host and port correctly");
        }
    }
}
