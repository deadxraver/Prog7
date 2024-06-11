package serverlogic;

public class ServerMain {
    public static void main(String... args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("No suitable driver found");
            System.exit(1);
        }
        new ServerProgram().run();
    }
}
