package exceptions;

public class EmptyStringException extends Exception {
    @Override
    public String getMessage() {
        return "String cannot be empty";
    }
}
