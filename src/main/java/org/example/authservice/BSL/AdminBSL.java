package org.example.authservice.BSL;



import org.example.authservice.DB.DatabaseManager;
import org.example.authservice.model.Instructor;
import org.example.authservice.model.Student;
import org.example.authservice.model.User;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.Hashing;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AdminBSL {
	@Inject
	GeneralResponse generalResponse;
	@Inject
	Hashing hashing;

	public Response ViewInstructorsAccounts() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Object> users = new ArrayList<>(); // List of either User or Instructor objects

		try {
			connection = DatabaseManager.getConnection();
			String query = "SELECT Users.id, Users.name, Users.email ,Users.bio ,Users.role, Instructors.affiliation, Instructors.years_experience " +
					"FROM Users " +
					"JOIN Instructors ON Users.id = Instructors.id";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				// Retrieve user attributes from the result set
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String affiliation = resultSet.getString("affiliation");
				int yearsExperience = resultSet.getInt("years_experience");
				String bio = resultSet.getString("bio");
				String role = resultSet.getString("role");
				Instructor instructor = new Instructor(id,name, email, affiliation, bio, role, yearsExperience);
				users.add(instructor);
			}
			return Response.status(HttpServletResponse.SC_OK).entity(users).build();
		} catch (SQLException e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while retrieving user accounts");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public Response ViewStudentAccounts() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Object> users = new ArrayList<>(); // List of either User or Instructor objects

		try {
			connection = DatabaseManager.getConnection();
			String query = "SELECT Users.id, Users.name, Users.email, Users.bio, Users.role, Students.affiliation " +
					"FROM Users " +
					"JOIN Students ON Users.id = Students.id";

			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				// Retrieve user attributes from the result set
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String affiliation = resultSet.getString("affiliation");
				String bio = resultSet.getString("bio");
				String role = resultSet.getString("role");
				Student student = new Student(id,name, email, affiliation, bio, role);
				users.add(student);
			}
			return Response.status(HttpServletResponse.SC_OK).entity(users).build();
		} catch (SQLException e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while retrieving user accounts");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Response ViewCenterTestAccounts() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Object> users = new ArrayList<>(); // List of either User or Instructor objects

		try {
			connection = DatabaseManager.getConnection();
			String query = "SELECT * FROM Users WHERE role = 'tester'";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String bio = resultSet.getString("bio");
				String role = resultSet.getString("role");

				User user = new User(id,name, email, bio, role);
				users.add(user);
			}
			return Response.status(HttpServletResponse.SC_OK).entity(users).build();
		} catch (SQLException e) {
			e.printStackTrace();
			generalResponse = new GeneralResponse("An error occurred while retrieving user accounts");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



}

