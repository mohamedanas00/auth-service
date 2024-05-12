package org.example.authservice.BSL;

import org.example.authservice.DB.DatabaseManager;
import org.example.authservice.model.Account;
import org.example.authservice.model.Instructor;
import org.example.authservice.model.Logs;
import org.example.authservice.model.User;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.model.response.SignInResponse;
import org.example.authservice.model.response.TokenResponse;
import org.example.authservice.utils.AuthUtil;
import org.example.authservice.utils.Hashing;
import org.example.authservice.utils.TestCenterGenerator;
import org.example.authservice.utils.TokenService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class AuthBSL {
	@Inject
	Hashing hashing;
	@Inject
	GeneralResponse generalResponse;
	@Inject
	AuthUtil authUtil;

	public Response signIn(String email, String password) throws IOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String message = null;
		try {
			connection = DatabaseManager.getConnection();

			// Check if user already exists
			if (!authUtil.userExists(email,connection)) {
				message="User NOT Exist please Register first";
				generalResponse = new GeneralResponse(message);
				return Response.status(HttpServletResponse.SC_CONFLICT).entity(generalResponse).build();
			}
			password=hashing.doHashing(password);

			// Query to check if the user exists with the provided username and password
			String query = "SELECT * FROM Users WHERE BINARY email = ? AND password = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);

			// Executing the query
			resultSet = preparedStatement.executeQuery();


			// If the result set has at least one row, authentication is successful
			if (resultSet.next()){

				String userRole = resultSet.getString("role");
				int userId = resultSet.getInt("id");
				String name = resultSet.getString("name");

				Map<String, Object> payload = new HashMap<>();
				payload.put("id", userId);
				payload.put("role", userRole);
				payload.put("name", name);
				payload.put("email", email);

				String token = TokenService.generateToken(payload);
				TokenResponse tokenResponse = new TokenResponse(token,userRole);
				insertLog(connection,userId,email,userRole,"logged in successfully");
				return Response.status(HttpServletResponse.SC_OK).entity(tokenResponse).build();
			} else {
				message="Invalid email or password!";
				generalResponse = new GeneralResponse(message);
				return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(generalResponse).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			message="An error occurred during authentication!";
			generalResponse = new GeneralResponse(message);
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(generalResponse).build();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Response RegisterUser(JsonObject jsonObject) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String message = null;
		try {
			connection = DatabaseManager.getConnection();

			// Check if user already exists
			String email = jsonObject.getString("email");
			if (authUtil.userExists(email, connection)) {
				message = "User already exists";
				return Response.status(HttpServletResponse.SC_CONFLICT)
						.entity(new GeneralResponse(message)).build();
			}

			// Hash the password before storing
			String password = jsonObject.getString("password");
			String hashedPassword = hashing.doHashing(password);

			String role = jsonObject.getString("role");
			String bio = jsonObject.getString("bio");

			String query = "";
			if (role.equals("instructor")||role.equals("student")) {
				query = "INSERT INTO Users (name, email, password, role,bio) VALUES (?, ?, ?, ?, ?)";
			} else {
				message = "Invalid role specified";
				return Response.status(HttpServletResponse.SC_BAD_REQUEST)
						.entity(new GeneralResponse(message)).build();
			}

			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, jsonObject.getString("name"));
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, hashedPassword);
			preparedStatement.setString(4, role);
			preparedStatement.setString(5, bio);

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				// Retrieve the generated user_id
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				int id = -1;
				if (generatedKeys.next()) {
					id = generatedKeys.getInt(1);
				}

				if (role.equals("instructor")) {
					query = "INSERT INTO Instructors (id, affiliation, years_experience) VALUES (?, ?, ?)";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, id);
					preparedStatement.setString(2, jsonObject.getString("affiliation"));
					preparedStatement.setInt(3, jsonObject.getInt("years_experience"));
					preparedStatement.executeUpdate();
				} else if (role.equals("student")) {
					query = "INSERT INTO Students (id, affiliation) VALUES (?, ?)";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, id);
					preparedStatement.setString(2, jsonObject.getString("affiliation"));
					preparedStatement.executeUpdate();
				}
				insertLog(connection,id,email,role,"registered successfully");

				message = "User created successfully";
				return Response.status(HttpServletResponse.SC_CREATED)
						.entity(new GeneralResponse(message)).build();
			} else {
				message = "Failed to create user";
				return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
						.entity(new GeneralResponse(message)).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			message = "An error occurred during user registration";
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.entity(new GeneralResponse(message)).build();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Response CreateTestCenterAccount(JsonObject jsonObject) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String message = null;

		try {
			connection = DatabaseManager.getConnection();

			// Additional check to ensure name not found for any tester
			String name = jsonObject.getString("name");
			if (userNameExistsForTester(name, connection)) {
				message = "Name already exists for a TesterCenter";
				return Response.status(HttpServletResponse.SC_CONFLICT)
						.entity(new GeneralResponse(message)).build();
			}
			String email = TestCenterGenerator.generateEmail(name);

			String password = TestCenterGenerator.generateRandomPassword();
			String hashedPassword = hashing.doHashing(password);

			String query = "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)";


			preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, hashedPassword);
			preparedStatement.setString(4, "tester");

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				message = "Test Center Account created successfully";
				Account account = new Account(email, password,message);
				return Response.status(HttpServletResponse.SC_CREATED)
						.entity(account).build();
			} else {
				message = "Failed to create Test Center Account ";
				return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
						.entity(new GeneralResponse(message)).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			message = "An error occurred during Create Test Center Account";
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.entity(new GeneralResponse(message)).build();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean userNameExistsForTester(String name, Connection connection) throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM Users WHERE REPLACE(LOWER(name), ' ', '') = REPLACE(LOWER(?), ' ', '') AND role = 'tester'";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt("count");
					return count > 0;
				}
			}
		}
		return false;
	}

	private void insertLog(Connection connection, int userId, String email, String role, String action) {
		PreparedStatement preparedStatement = null;
		try {
			// Inserting log into the database
			String insertLogQuery = "INSERT INTO logs (userId, email, role, action) VALUES (?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(insertLogQuery);
			preparedStatement.setInt(1, userId);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, role);
			preparedStatement.setString(4, action);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println("An error occurred while inserting log: " + e.getMessage());
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				System.out.println("An error occurred while inserting log: " + e.getMessage());
			}
		}
	}

}