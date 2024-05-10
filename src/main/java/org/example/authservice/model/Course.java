package org.example.authservice.model;

public class Course {
	private int id;
	private int instructorId;
	private String name;
	private double duration;
	private String category;
	private double rating;
	private int capacity;
	private int enrolledCount;
	private String instructorName;
	private boolean isPublished;

	public Course() {
	}

	public Course(String name, double duration, String category, int capacity) {
		this.name = name;
		this.duration = duration;
		this.category = category;
		this.capacity = capacity;
	}

	public Course(int id, int instructorId, String name, double duration, String category, double rating, int capacity, int enrolledCount, String instructorName) {
		this.id = id;
		this.instructorId = instructorId;
		this.name = name;
		this.duration = duration;
		this.category = category;
		this.rating = rating;
		this.capacity = capacity;
		this.enrolledCount = enrolledCount;
		this.instructorName = instructorName;
	}

	public Course(int id, int instructorId, String name, double duration, String category, double rating, int capacity, int enrolledCount, String instructorName, boolean isPublished) {
		this.id = id;
		this.instructorId = instructorId;
		this.name = name;
		this.duration = duration;
		this.category = category;
		this.rating = rating;
		this.capacity = capacity;
		this.enrolledCount = enrolledCount;
		this.instructorName = instructorName;
		this.isPublished = isPublished;
	}

	// Getters and setters for all fields
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(int instructorId) {
		this.instructorId = instructorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getEnrolledCount() {
		return enrolledCount;
	}

	public void setEnrolledCount(int enrolledCount) {
		this.enrolledCount = enrolledCount;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public boolean isPublished() {
		return isPublished;
	}

	public void setPublished(boolean published) {
		isPublished = published;
	}
}
