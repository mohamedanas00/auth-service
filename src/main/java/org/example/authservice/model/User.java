package org.example.authservice.model;

public class User {
	private int id;
	private String name;
	private String email;
	private String bio;
	private String role;

	public User(){}

	public User(int id,String name, String email, String bio, String role) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.bio = bio;
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
