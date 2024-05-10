package org.example.authservice.model.response;

public class SignInResponse {
	private int id;
	private String role;

	public SignInResponse(int id, String role) {
		this.id = id;
		this.role = role;
	}

	public SignInResponse(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
