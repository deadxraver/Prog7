package exceptions;

public class ParseException extends Exception {
    @Override
    public String getMessage() {
        return "Element cannot be parsed";
    }
}
