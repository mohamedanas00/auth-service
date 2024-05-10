package org.example.authservice.model;

public class Student extends User{
	private int years_experience;
	private  String affiliation;

	public Student() {}

	public Student(int id,String name, String email, String affiliation, String bio, String role) {
		super(id,name, email, bio, role);
		this.affiliation = affiliation;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
}
