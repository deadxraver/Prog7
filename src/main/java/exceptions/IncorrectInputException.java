package exceptions;

public class IncorrectInputException extends Exception {
    @Override
    public String getMessage() {
        return "Incorrect input";
    }
}
