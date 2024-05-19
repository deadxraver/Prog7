package exceptions;

public class DateIsNotReachedException extends ValidationException {
    @Override
    public String getMessage() {
        return "Date is not yet reached";
    }
}
