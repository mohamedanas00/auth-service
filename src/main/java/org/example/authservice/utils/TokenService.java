package org.example.authservice.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TokenService {

	// Specify your secret key here (make sure it's a byte array)
	private static final byte[] SECRET_KEY_BYTES = {
			(byte) 0x53, (byte) 0x65, (byte) 0x63, (byte) 0x72,
			(byte) 0x65, (byte) 0x74, (byte) 0x4b, (byte) 0x65,
			(byte) 0x79, (byte) 0x2e, (byte) 0x46, (byte) 0x6f,
			(byte) 0x72, (byte) 0x4a, (byte) 0x61, (byte) 0x76,
			(byte) 0x61, (byte) 0x2e, (byte) 0x53, (byte) 0x65,
			(byte) 0x63, (byte) 0x72, (byte) 0x65, (byte) 0x74,
			(byte) 0x4b, (byte) 0x65, (byte) 0x79, (byte) 0x2e,
			(byte) 0x53, (byte) 0x65, (byte) 0x63, (byte) 0x72,
			(byte) 0x65, (byte) 0x74, (byte) 0x4b, (byte) 0x65
	};

	private static final SecretKey SECRET_KEY = new SecretKeySpec(SECRET_KEY_BYTES, SignatureAlgorithm.HS256.getJcaName());

	public static String generateToken(Map<String, Object> payload) {
		long expiresInSeconds = 60 * 60 * 24 * 365;
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiresInSeconds * 1000);

		return Jwts.builder()
				.setClaims(payload)
				.setExpiration(expiryDate)
				.signWith(SECRET_KEY)
				.compact();
	}

	public static Map<String, Object> verifyToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(SECRET_KEY)
					.build()
					.parseClaimsJws(token)
					.getBody();

			// Convert Claims to Map
			return new HashMap<>(claims);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}



