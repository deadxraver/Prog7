package clientlogic;

import exceptions.WrongNumberOfArgumentsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.Scanner;

import static sun.security.util.Password.readPassword;

public class Program {
    private final int port;
    private final String host;
    private String username;
    private String password;
    private final Scanner reader;
    public Program(String[] args) throws WrongNumberOfArgumentsException {
        reader = new Scanner(System.in);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("connection_details_en");
        if (args.length == 0) {
            this.port = Integer.parseInt(resourceBundle.getString("port"));
            this.host = resourceBundle.getString("host");
        } else if (args.length == 1) {
            this.port = Integer.parseInt(args[0]);
            this.host = resourceBundle.getString("host");
        } else if (args.length == 2) {
            this.port = Integer.parseInt(args[1]);
            this.host = args[0];
        } else throw new WrongNumberOfArgumentsException();
    }
    public Program() {
        reader = new Scanner(System.in);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("connection_details_en");
        this.port = Integer.parseInt(resourceBundle.getString("port"));
        this.host = resourceBundle.getString("host");
    }
    public void run() {

    }
    private void authorization() {
        try {
            System.out.println("login:");
            this.username = reader.nextLine();
            System.out.println("password:");
            this.password = getPassword();
        } catch (NoSuchAlgorithmException | IOException ignored) {}
    }

    private String getPassword() throws IOException, NoSuchAlgorithmException { // todo перенести на сервер
        String temp = new String(readPassword(System.in)).trim();
//        MessageDigest md = MessageDigest.getInstance("SHA-224");
//        byte[] data = temp.getBytes(StandardCharsets.UTF_8);
//        byte[] hashBytes = md.digest(data);
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : hashBytes) {
//            hexString.append(String.format("%02x", b));
//        }
//        temp = hexString.toString();
        return temp;
    }
}
