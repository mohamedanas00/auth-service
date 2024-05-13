package org.example.authservice.BSL;



import org.example.authservice.DB.DatabaseConnectionManager;
import org.example.authservice.model.Instructor;
import org.example.authservice.model.Student;
import org.example.authservice.model.User;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.AuthUtil;
import org.example.authservice.utils.Hashing;

import javax.ejb.EJB;
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
	AuthUtil authUtil;
	@Inject
	Hashing hashing;
	@EJB
	DatabaseConnectionManager connectionManager;

	public Response ViewInstructorsAccounts() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Object> users = new ArrayList<>(); // List of either User or Instructor objects

		try {
			connection = connectionManager.getConnection();
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
			connection = connectionManager.getConnection();
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
			connection = connectionManager.getConnection();
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Response DeleteAccount(int id){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		if (id <= 0) {
			generalResponse=new GeneralResponse("Invalid user ID.");
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(generalResponse).build();
		}
		try {
			connection = connectionManager.getConnection();

			String query = "DELETE FROM Users WHERE id = ?";
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setInt(1, id);
			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				generalResponse=new GeneralResponse("User with ID " + id + " deleted successfully.");
				return Response.status(HttpServletResponse.SC_OK).entity(generalResponse).build();
			} else {
				generalResponse=new GeneralResponse("User with ID " + id + " not found.");
				return Response.status(HttpServletResponse.SC_NOT_FOUND)
						.entity(generalResponse).build();
			}

		} catch (SQLException e) {
			generalResponse=new GeneralResponse("An error occurred while deleting user with ID " + id + ".");
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.entity(generalResponse).build();
		}finally {
			try {
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Response UpdateUserAccount(JsonObject jsonObject,int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String message = null;
		try {
			connection = connectionManager.getConnection();

			String email = jsonObject.getString("email");
			if (!authUtil.userExistsById(userId, connection)) {
				message = "User Not Exist";
				return Response.status(Response.Status.NOT_FOUND)
						.entity(new GeneralResponse(message))
						.build();
			}

			// Hash the password before storing, if present
			String password = jsonObject.containsKey("password") ? jsonObject.getString("password") : null;
			String hashedPassword = null;
			if (password != null) {
				hashedPassword = hashing.doHashing(password);
			}

			// Extract other fields if present
			String bio = jsonObject.containsKey("bio") ? jsonObject.getString("bio") : null;

			// Update the database
			StringBuilder updateQuery = new StringBuilder("UPDATE Users SET");
			List<String> setClauses = new ArrayList<>();
			if (hashedPassword != null) {
				setClauses.add(" password = ?");
			}
			if (bio != null) {
				setClauses.add(" bio = ?");
			}
			updateQuery.append(String.join(",", setClauses));
			updateQuery.append(" WHERE id = ?");

			preparedStatement = connection.prepareStatement(updateQuery.toString());
			int parameterIndex = 1;
			if (hashedPassword != null) {
				preparedStatement.setString(parameterIndex++, hashedPassword);
			}
			if (bio != null) {
				preparedStatement.setString(parameterIndex++, bio);
			}
			preparedStatement.setInt(parameterIndex, userId);

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				message = "Test Center Account updated successfully";
				return Response.status(HttpServletResponse.SC_OK).entity(new GeneralResponse(message)).build();
			}
			message = "Error updating Test Center Account";
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new GeneralResponse(message)).build();

		} catch (SQLException e) {
			e.printStackTrace();
			message = "Error updating Test Center Account";
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new GeneralResponse(message))
					.build();
		} finally {
			try {
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}





}

