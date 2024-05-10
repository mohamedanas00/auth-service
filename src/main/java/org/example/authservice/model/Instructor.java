package org.example.authservice.model;

public class Instructor extends User {
	private int years_experience;
	private  String affiliation;

	public Instructor() {}

	// Constructor with all parameters
	public Instructor(int id,String name, String email, String affiliation, String bio, String role, int years_experience) {
		super(id,name, email, bio, role);
		this.affiliation = affiliation;
		this.years_experience = years_experience;
	}

	public int getYears_experience() {
		return years_experience;
	}

	public void setYears_experience(int years_experience) {
		this.years_experience = years_experience;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
}
