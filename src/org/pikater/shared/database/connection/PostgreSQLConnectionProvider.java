package org.pikater.shared.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class {@link PostgreSQLConnectionProvider} is used to create a native database connection
 * to PostgreSQL database management system.
 */
public class PostgreSQLConnectionProvider implements IConnectionProvider {
	private final String url;
	private final String userName;
	private final String password;

	/**
	 * Constructor for setting the data needed to establish a new connection
	 * @param url of the database management system
	 * @param username to be used for the connection
	 * @param password to be used for the connection
	 */
	public PostgreSQLConnectionProvider(String url, String username, String password) {
		this.url = url;
		this.userName = username;
		this.password = password;
	}

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

	@Override
	public String getConnectionInfo() {
		return url;
	}

	@Override
	public String getSchema() {
		return "public";
	}

}
