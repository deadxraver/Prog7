package exceptions;

public class NoSuchMovieException extends Exception {
    @Override
    public String getMessage() {
        return "No movie with such index";
    }
}
