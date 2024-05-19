package exceptions;

public class ValidationException extends Exception {
    @Override
    public String getMessage() {
        return "Element does not pass required checks";
    }
}
