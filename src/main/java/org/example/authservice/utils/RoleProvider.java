package org.example.authservice.utils;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleProvider {

	@RoleQualifier("admin")
	public String getAdminRole() {
		return "admin";
	}

	@RoleQualifier("instructor")
	public String getInstructorRole() {
		return "instructor";
	}

	@RoleQualifier("student")
	public String getStudentRole() {
		return "student";
	}


	@RoleQualifier("tester")
	public String getTestCenterRole() {
		return "tester";
	}
}