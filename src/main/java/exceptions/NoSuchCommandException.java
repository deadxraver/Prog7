package exceptions;

public class NoSuchCommandException extends ParseException {
    @Override
    public String getMessage() {
        return "No such command";
    }
}
