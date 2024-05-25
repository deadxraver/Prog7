package exceptions;

public class NoSuchMpaaRatingException extends ParseException {
    @Override
    public String getMessage() {
        return "No such MPAA rating";
    }
}
