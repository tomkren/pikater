package org.pikater.shared.database.postgre;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.pikater.core.CoreConfiguration;
import org.pikater.shared.database.connection.PostgreSQLConnectionProvider;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.logging.database.PikaterDBLogger;
import org.postgresql.PGConnection;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
			ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(CoreConfiguration.BEANS_CONFIG_FILE);
			con=(PGConnection)(
					(PostgreSQLConnectionProvider)context.getBean("defaultConnection")
					).getConnection();
			context.close();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			PikaterDBLogger.logThrowable("Can't establish connection to Database", e);
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
			q.getSingleResult();
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