package org.example.authservice.model.response;

public class TokenResponse {
	String token;
	String role;
	public TokenResponse() {

	}
	public TokenResponse(String token,String role) {
		this.token = token;
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
