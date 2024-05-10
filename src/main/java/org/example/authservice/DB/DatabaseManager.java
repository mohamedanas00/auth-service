package org.example.authservice.DB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	    private static final String URL = "jdbc:mysql://ugn8wvfwagy5gpql:B7lwsqiCFvDVi1aSBp9Y@b970a8wpyg3u7bs2yrii-mysql.services.clever-cloud.com:3306/b970a8wpyg3u7bs2yrii";
		private static final String USERNAME = "ugn8wvfwagy5gpql";
		private static final String PASSWORD = "B7lwsqiCFvDVi1aSBp9Y";

		public static Connection getConnection() throws SQLException {
			Connection connection = null;
			try {
				// Load the MySQL JDBC driver
				Class.forName("com.mysql.cj.jdbc.Driver");

				// Establish the connection
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return connection;
		}

}
