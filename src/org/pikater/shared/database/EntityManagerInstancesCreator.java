package org.pikater.shared.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.pikater.shared.utilities.logging.PikaterLogger;

public class EntityManagerInstancesCreator
{
	/**
	 * EntityManagerFactory instantiation takes some time so it's best to use a single instance to
	 * produce EntityManager instances, unless we need several different configurations for various
	 * purposes.
	 */
	public static EntityManagerFactory primaryFactory;
	
	// TODO: use static JVM-wide logger with a nice interface instead?
	/**
	 * @see org.pikater.shared.logging.PikaterLogger
	 */
	private static Logger logger=PikaterLogger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	
	static
	{
		try
		{
			primaryFactory = Persistence.createEntityManagerFactory("pikaterDataModel");
		}
		catch(Throwable t)
		{
			logger.log(Level.ERROR, "Failed to initialize EntityManagerFactory: ", t);
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
}
