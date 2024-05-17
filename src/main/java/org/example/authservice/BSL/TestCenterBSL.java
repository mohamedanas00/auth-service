package org.example.authservice.BSL;


import org.example.authservice.DB.DatabaseConnectionManager;
import org.example.authservice.model.TestCenterService;
import org.example.authservice.model.response.GeneralResponse;
import org.example.authservice.utils.AuthUtil;
import org.example.authservice.utils.TestCenterUpdateService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TestCenterBSL {
	@Inject
	AuthUtil authUtil;
	@EJB
	private DatabaseConnectionManager connectionManager;


	public Response UpdateUserAccount(JsonObject jsonObject, int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String message = null;
		try {
			connection = connectionManager.getConnection();

			// Check if user exists
			if (!authUtil.userExistsById(userId, connection)) {
				message = "User Not Exist";
				return Response.status(Response.Status.NOT_FOUND)
						.entity(new GeneralResponse(message))
						.build();
			}

			// Extract fields to update
			String name = jsonObject.containsKey("name") ? jsonObject.getString("name") : null;
			String email = jsonObject.containsKey("email") ? jsonObject.getString("email") : null;
			String bio = jsonObject.containsKey("bio") ? jsonObject.getString("bio") : null;

			// Check if the new name already exists for users with role "tester"
			if (name != null && authUtil.isNameAlreadyExistsForTester(name, connection)) {
				message = "Name Already Exists for Test Center ";
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new GeneralResponse(message))
						.build();
			}
			TestCenterUpdateService testCenterUpdateService = new TestCenterUpdateService();
			if(!testCenterUpdateService.updateTestCenter(userId, new TestCenterService(name,email))) {
				message = "please try again later";
				return Response.status(Response.Status.NOT_FOUND)
						.entity(new GeneralResponse(message))
						.build();
			}
			// Update the database
			StringBuilder updateQuery = new StringBuilder("UPDATE Users SET");
			List<String> setClauses = new ArrayList<>();
			List<Object> values = new ArrayList<>();
			if (name != null) {
				setClauses.add(" name = ?");
				values.add(name);
			}
			if (email != null) {
				if (authUtil.userExists(email, connection)) {
					message = "Email Already Exists";
					return Response.status(Response.Status.BAD_REQUEST)
							.entity(new GeneralResponse(message))
							.build();
				}
				setClauses.add(" email = ?");
				values.add(email);
			}
			if (bio != null) {
				setClauses.add(" bio = ?");
				values.add(bio);
			}
			if (setClauses.isEmpty()) {
				message = "No fields to update";
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new GeneralResponse(message))
						.build();
			}

			updateQuery.append(String.join(",", setClauses));
			updateQuery.append(" WHERE id = ?");

			preparedStatement = connection.prepareStatement(updateQuery.toString());
			int parameterIndex = 1;
			for (Object value : values) {
				preparedStatement.setObject(parameterIndex++, value);
			}
			preparedStatement.setInt(parameterIndex, userId);

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				message = "User Account updated successfully";
				return Response.status(HttpServletResponse.SC_OK)
						.entity(new GeneralResponse(message))
						.build();
			}
			message = "Error updating User Account";
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new GeneralResponse(message))
					.build();

		} catch (SQLException e) {
			e.printStackTrace();
			message = "Error updating User Account";
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new GeneralResponse(message))
					.build();
		} finally {
			try {
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
