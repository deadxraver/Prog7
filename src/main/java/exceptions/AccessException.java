package exceptions;

public class AccessException extends SecurityException {
    @Override
    public String getMessage() {
        return "You cannot modify this movie";
    }
}
