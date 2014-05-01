package org.pikater.shared.utilities.pikaterDatabase.newDB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.pikater.shared.utilities.logging.PikaterLogger;



public class EntityManagerInstancesCreator
{
	/*
	 * - EntityManagerFactory instantiation takes some time so it's best to use a single instance to produce
	 * EntityManager instances.
	 * - Produced EntityManager instances use the parental EntityManagerFactory configurations 
	 * (see \war\WEB-INF\classes\META-INF\persistence.xml).
	 */
	public static EntityManagerFactory emfInstance;
	
	private static Logger logger=PikaterLogger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	
	static
	{
		try
		{
			emfInstance = Persistence.createEntityManagerFactory("pikaterDataModel");
		}
		catch(Throwable t)
		{
			logger.log(Level.ERROR, "Failed to initialize EntityManagerFactory: ", t);
		}
	}

	public static EntityManager getEntityManagerInstance()
	{
		return emfInstance.createEntityManager();
    }
}
