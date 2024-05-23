package exceptions;

public class NoSuchMpaaRating extends ParseException {
    @Override
    public String getMessage() {
        return "No such MPAA rating";
    }
}
