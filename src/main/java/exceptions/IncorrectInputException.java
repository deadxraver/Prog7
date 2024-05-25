package exceptions;

public class IncorrectInputException extends ParseException {
    @Override
    public String getMessage() {
        return "Incorrect input";
    }
}
