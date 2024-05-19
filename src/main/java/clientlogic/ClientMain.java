package clientlogic;

import exceptions.WrongNumberOfArgumentsException;

public class ClientMain {
    public static void main(String... args) {
        try {
            new Program(args).run();
        } catch (WrongNumberOfArgumentsException | NumberFormatException e) {
            System.err.println(e.getMessage());
            System.err.print("Try: ");
            System.out.println("java -jar client.jar [ port | host port ]");
        }
    }
}
