package org.pikater.shared.database.postgre;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.pikater.shared.database.connection.PostgreSQLConnectionProvider;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.daos.DAOs;
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
	
	/**
	 * Test Database connectivity with testing following features
	 * 1. accessibility of Postgre connection for Large Objects
	 * 2. accessibility of entity table
	 * @return true if database is reachable
	 */
	public static boolean isConnectionToCurrentPGDBEstablished()
	{
		if(con==null){
			return false;
		}
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		Query q=em.createNativeQuery("select count(*) from "+DAOs.userDAO.getEntityName());
		try{
			@SuppressWarnings("unused")
			Long bg = (Long) q.getSingleResult();
		}catch(Exception e){
			return false;
		}
		return true;

	}
	
	public static PGConnection getConnectionToCurrentPGDB()
	{
		return con;
	}
}