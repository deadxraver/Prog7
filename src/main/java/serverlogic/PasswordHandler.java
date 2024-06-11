package serverlogic;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHandler {
	private PasswordHandler() {
	}

	public static final char FIRST_SYMBOL = '!';
	public static final char LAST_SYMBOL = '~';

	public static String generateSalt() {
		int length = (int) (Math.random() * 20 + 1);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			stringBuilder.append((char) ((int) (Math.random() * (LAST_SYMBOL - FIRST_SYMBOL)) + FIRST_SYMBOL));
		}
		return stringBuilder.toString();
	}

	public static String encrypt(String passwordWithSalt) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-224");
			byte[] messageDigest = md.digest(passwordWithSalt.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			StringBuilder hashText = new StringBuilder(no.toString(16));
			while (hashText.length() < 32) {
				hashText.insert(0, "0");
			}
			return hashText.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
