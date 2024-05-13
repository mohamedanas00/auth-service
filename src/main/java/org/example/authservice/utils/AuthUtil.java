package org.example.authservice.utils;



import org.example.authservice.DB.DatabaseConnectionManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.SecureRandom;
import java.util.Random;

@Stateless
public class AuthUtil {
	@EJB
	DatabaseConnectionManager connectionManager;
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
		try (Connection connection = connectionManager.getConnection();
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

	public boolean userExistsById(int id, Connection connection) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String query = "SELECT COUNT(*) FROM Users WHERE id = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
			return false;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}


	public boolean userExists(String email,Connection connection) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String query = "SELECT COUNT(*) FROM Users WHERE BINARY email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
			return false;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}

	public boolean isNameAlreadyExistsForTester(String name, Connection connection) throws SQLException {
		String query = "SELECT COUNT(*) FROM Users WHERE name = ? AND role = 'tester'";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt(1);
					return count > 0;
				}
			}
		}
		return false;
	}





}