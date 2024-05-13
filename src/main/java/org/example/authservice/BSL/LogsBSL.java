package org.example.authservice.BSL;

import org.example.authservice.DB.DatabaseConnectionManager;
import org.example.authservice.model.Logs;
import org.example.authservice.model.response.GeneralResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LogsBSL {
	@EJB
	private DatabaseConnectionManager connectionManager;

	public Response GetLogs() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String message = null;
		List<Logs> logsList = new ArrayList<>();

		try {
			connection = connectionManager.getConnection();
			String query = "SELECT * FROM logs";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Logs logs = new Logs();
				logs.setId(resultSet.getInt("id"));
				logs.setUserId(resultSet.getInt("userId"));
				logs.setEmail(resultSet.getString("email"));
				logs.setRole(resultSet.getString("role"));
				logs.setAction(resultSet.getString("action"));
				logs.setCreatedAt(resultSet.getTimestamp("created_at"));
				logsList.add(logs);
			}
			return Response.status(HttpServletResponse.SC_OK).entity(logsList).build();

		} catch (SQLException e) {
			message = "An error occurred during Getting Logs";
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.entity(new GeneralResponse(message)).build();
		} finally {
			// Close resources in finally block
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
