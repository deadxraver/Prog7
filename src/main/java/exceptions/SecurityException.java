package exceptions;

public class SecurityException extends Exception {
    @Override
    public String getMessage() {
        return "This action is not secure or breaks other users confidentiality";
    }
}
