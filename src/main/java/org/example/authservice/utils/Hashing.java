package org.example.authservice.utils;

import javax.ejb.Stateless;
import java.security.MessageDigest;

@Stateless
public class Hashing {
	public String doHashing(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(password.getBytes());
			byte[] resultByte = messageDigest.digest();
			StringBuilder builder = new StringBuilder();
			for (byte b : resultByte) {
				builder.append(String.format("%02x", b));
			}
			return builder.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		return password;
	}

	public  boolean comparePassword(String Password, String hashedPassword) {
		String hashedEnteredPassword = doHashing(Password);
		return hashedEnteredPassword.equals(hashedPassword);
	}
}
