package exceptions;

public class NoSuchCountryException extends ParseException {
    @Override
    public String getMessage() {
        return "No such country";
    }
}
