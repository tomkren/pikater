package org.pikater.shared.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnectionProvider implements IConnectionProvider
{
    private final String url;
    private final String userName;
    private final String password;

    public PostgreSQLConnectionProvider(String url, String userName, String password)
    {
        this.url = url;
        this.userName = userName;
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
