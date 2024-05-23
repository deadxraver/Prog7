package exceptions;

import java.io.IOException;

public class AuthorizationException extends IOException {
    @Override
    public String getMessage() {
        return "Could not authorize";
    }
}
