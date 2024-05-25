package exceptions;

public class NoSuchColorException extends ParseException {
    @Override
    public String getMessage() {
        return "No such color";
    }
}
