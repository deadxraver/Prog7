package exceptions;

public class NoSuchGenreException extends ParseException {
    @Override
    public String getMessage() {
        return "No such genre";
    }
}
