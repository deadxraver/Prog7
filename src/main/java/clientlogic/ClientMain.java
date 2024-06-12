package clientlogic;

import elements.MpaaRating;

public class ClientMain {
    public static void main(String... args) {
        try {
            new ClientProgram().run();
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            System.err.print("Try: ");
            System.out.println("java -jar client.jar [ {port} | {host} {port} ]");
        }
    }
}
