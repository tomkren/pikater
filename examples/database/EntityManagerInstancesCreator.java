package vietpot.server.Database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import vietpot.server.ErrorLogger;

public class EntityManagerInstancesCreator
{
	/*
	 * - EntityManagerFactory instantiation takes some time so it's best to use a single instance to produce
	 * EntityManager instances.
	 * - Produced EntityManager instances use the parental EntityManagerFactory configurations 
	 * (see \war\WEB-INF\classes\META-INF\persistence.xml).
	 */
	public static EntityManagerFactory emfInstance;
	
	static
	{
		try
		{
			emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
		}
		catch(Throwable t)
		{
			ErrorLogger.logThrowable("Failed to initialize EntityManagerFactory: ", t);
		}
	}

	public static EntityManager getEntityManagerInstance()
	{
		return emfInstance.createEntityManager();
    }
}
