package org.example.authservice.model.response;

public class TokenResponse {
	String token;
	String name;
	String role;
	public TokenResponse() {

	}
	public TokenResponse(String token,String name,String role) {
		this.token = token;
		this.role = role;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
