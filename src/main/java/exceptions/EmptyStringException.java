package exceptions;

public class EmptyStringException extends ValidationException {
    @Override
    public String getMessage() {
        return "String cannot be empty";
    }
}
