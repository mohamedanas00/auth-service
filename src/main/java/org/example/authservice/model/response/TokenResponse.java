package org.example.authservice.model.response;

public class TokenResponse {
	String token;

	public TokenResponse() {

	}
	public TokenResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
