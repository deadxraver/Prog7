package exceptions;

import java.sql.SQLException;

public class PasswordException extends SQLException {
	@Override
	public String getMessage() {
		return "Passwords do not match";
	}
}
