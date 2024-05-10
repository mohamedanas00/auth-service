package org.example.authservice.utils;


import org.example.authservice.DB.DatabaseManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Stateless
public class AuthUtil {

	@Inject
	RoleProvider roleProvider;

	public boolean isAdmin(int id) {
		String role = roleProvider.getAdminRole();
		return isValidUser(id, role);
	}

	public boolean isInstructor(int id) {
		String role = roleProvider.getInstructorRole();
		return isValidUser(id, role);
	}

	public boolean isStudent(int id) {
		String role = roleProvider.getStudentRole();
		return isValidUser(id, role);
	}


	public boolean isTestCenter(int id) {
		String role = roleProvider.getTestCenterRole();
		return isValidUser(id, role);
	}


	private boolean isValidUser(int id, String expectedRole) {
		String actualRole = getUserRoleById(id);
		return actualRole != null && actualRole.equals(expectedRole);
	}

	public String getUserRoleById(int id) {
		try (Connection connection = DatabaseManager.getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement("SELECT role FROM Users WHERE id = ?")) {
			preparedStatement.setInt(1, id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString("role");
				}else{
					return "null";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


}