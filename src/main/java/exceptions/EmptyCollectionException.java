package exceptions;

public class EmptyCollectionException extends ValidationException {
    @Override
    public String getMessage() {
        return "Collection is empty";
    }
}
