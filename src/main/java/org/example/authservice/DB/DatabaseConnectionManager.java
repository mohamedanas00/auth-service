package org.example.authservice.DB;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



@Singleton
@Startup
public class DatabaseConnectionManager {
	private static final String URL = "jdbc:mysql://ugn8wvfwagy5gpql:B7lwsqiCFvDVi1aSBp9Y@b970a8wpyg3u7bs2yrii-mysql.services.clever-cloud.com:3306/b970a8wpyg3u7bs2yrii";
	private static final String USERNAME = "ugn8wvfwagy5gpql";
	private static final String PASSWORD = "B7lwsqiCFvDVi1aSBp9Y";

	private Connection connection;

	public DatabaseConnectionManager() {}

	public synchronized Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			} catch (ClassNotFoundException e) {
				throw new SQLException("Failed to establish database connection", e);
			}
		}
		return connection;
	}
}
