package org.pikater.shared.database.postgre;

import java.sql.SQLException;

import org.pikater.shared.database.connection.PostgreSQLConnectionProvider;
import org.pikater.shared.logging.PikaterLogger;
import org.postgresql.PGConnection;

public class MyPGConnection
{
	/**
	 * A singleton connection instance. Used (at least) for all large object download/upload tasks.
	 */
	private static PGConnection con = null;
	static
	{
		try
		{
			con = (PGConnection) (new PostgreSQLConnectionProvider(
					"jdbc:postgresql://nassoftwerak.ms.mff.cuni.cz:5432/pikater",
					"pikater", "SrapRoPy").getConnection());
		}
		catch (ClassNotFoundException | SQLException e)
		{
			PikaterLogger.logThrowable("Can't establish connection to Database", e);
		}
	}
	
	public static boolean isConnectionToCurrentPGDBEstablished()
	{
		return con != null;
	}
	
	public static PGConnection getConnectionToCurrentPGDB()
	{
		return con;
	}
}