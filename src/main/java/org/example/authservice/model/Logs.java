package org.example.authservice.model;

import java.sql.Timestamp;

public class Logs {
	private int id;
	private int userId;
	private String email;
	private String role;
	private String action;
	private Timestamp createdAt;

	// Constructors
	public Logs() {
	}

	public Logs(int userId, String email, String role, String action) {
		this.userId = userId;
		this.email = email;
		this.role = role;
		this.action = action;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}
