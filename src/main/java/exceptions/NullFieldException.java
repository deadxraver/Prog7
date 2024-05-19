package exceptions;

public class NullFieldException extends ValidationException {
    @Override
    public String getMessage() {
        return "Field cannot be null";
    }
}
