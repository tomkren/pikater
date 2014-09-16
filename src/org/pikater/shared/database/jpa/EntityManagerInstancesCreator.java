package org.pikater.shared.database.jpa;

import java.net.UnknownHostException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.pikater.shared.logging.database.PikaterDBLogger;

public class EntityManagerInstancesCreator
{
	/**
	 * EntityManagerFactory instantiation takes some time so it's best to use a single instance to
	 * produce EntityManager instances, unless we need several different configurations for various
	 * purposes.
	 */
	private static EntityManagerFactory primaryFactory;
	static
	{
		try
		{
			primaryFactory = Persistence.createEntityManagerFactory("pikaterDataModel");
		}
		catch(Exception t)
		{
			PikaterDBLogger.logThrowable("Failed to initialize EntityManagerFactory: ", t);
		}
	}

	/**
	 * Produced instances of EntityManager use the same configuration as the parental EntityManagerFactory instance. 
	 * @see ./src/META-INF/persistence.xml
	 */
	public static EntityManager getEntityManagerInstance()
	{
		return primaryFactory.createEntityManager();
    }
	
	/**
	 * Makes a default database call from information provided in the "persistence.xml" file.
	 * @throws UnknownHostException If the database address provided in "persistence.xml" could not be resolved.
	 * 		Check internet connection on both machines, firewall settings, etc...</br>
	 * 		IMPORTANT NOTE: this behaviour might be specific to PostGre.
	 * @TODO: exception thrown when invalid auth info is provided in "persistence.xml"?
	 */
	public static void testDatabaseConnectionWorkingProperly() throws UnknownHostException
	{
		getEntityManagerInstance();		
	}
}
