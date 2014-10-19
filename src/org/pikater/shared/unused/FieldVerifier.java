package org.pikater.shared.unused;

public class FieldVerifier {
	public static boolean isStringNullOrEmpty(String arg) {
		return (arg == null) || arg.isEmpty();
	}

	public static boolean isValidEmail(String email) {
		if (!isStringNullOrEmpty(email)) {
			String[] splitted = email.split("@");
			// max email length: 64@255
			if ((splitted.length == 2) && (splitted[0].length() <= 64) && (splitted[1].length() <= 255) && splitted[1].contains(".")) {
				return true;
			}
		}
		return false;
	}
}
