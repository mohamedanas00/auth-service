package org.example.authservice.utils;

import java.security.SecureRandom;
import java.util.Random;

public class TestCenterGenerator {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int PASSWORD_LENGTH = 8;


	public static String generateEmail(String name) {
		String formattedName = name.replaceAll("\\s", ".");
		return formattedName + "@gmail.com";
	}

	public static String generateRandomPassword() {
			Random random = new SecureRandom();
			StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
			for (int i = 0; i < PASSWORD_LENGTH; i++) {
				password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
			}
			return password.toString();
	}

}

