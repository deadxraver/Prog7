package exceptions;

public class WrongNumberOfArgumentsException extends ValidationException {
    @Override
    public String getMessage() {
        return "Number of args cannot be greater than 2";
    }
}
