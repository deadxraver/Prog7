package exceptions;

public class NoSuchCountryException extends Exception {
    @Override
    public String getMessage() {
        return "No such country";
    }
}
