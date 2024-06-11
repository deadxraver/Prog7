package exceptions;

import java.sql.SQLException;

public class UserAlreadyExistsException extends SQLException {
	@Override
	public String getMessage() {
		return "This username is already taken, try another";
	}
}
