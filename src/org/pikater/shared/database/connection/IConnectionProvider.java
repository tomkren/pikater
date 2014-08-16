package org.pikater.shared.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: Kuba
 * Date: 9.11.13
 * Time: 19:48
 */
public interface IConnectionProvider {
    public Connection getConnection() throws ClassNotFoundException, SQLException;
    public String getConnectionInfo();
    public String getSchema();
}
