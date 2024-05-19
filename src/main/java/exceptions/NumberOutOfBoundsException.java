package exceptions;

public class NumberOutOfBoundsException extends ValidationException {
    @Override
    public String getMessage() {
        return "Number does not fit the requirements";
    }
}
