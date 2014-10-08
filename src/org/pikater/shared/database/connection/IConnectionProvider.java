package org.pikater.shared.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: Kuba
 * Date: 9.11.13
 * Time: 19:48
 */
public interface IConnectionProvider {
	/**
	 * Creates a new connection using the data set in the constructor.
	 * @return the {@link Connection} object
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    public Connection getConnection() throws ClassNotFoundException, SQLException;
    /**
     * Returns the connection information for the current connection. In most cases this is
     * the URL of the connection.
     * @return {@link String} describing connection information
     */
    public String getConnectionInfo();
    /**
     * Returns the name of schema used by the connection. 
     * @return {@link String} containing schema name
     */
    public String getSchema();
}
