package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.utilities.logging.PikaterLogger;

public abstract class AbstractDAO
{
	protected static Logger logger=PikaterLogger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	
	public enum EmptyResultAction
	{
		/**
		 * Log error if no result is found and return null.
		 */
		LOG_NULL,
		
		/**
		 * Don't log an error if no result is found and return null.
		 */
		NULL,
		
		/**
		 * Silently throw a runtime error to handle in the calling code if no result is found.
		 */
		THROW;
		
		public static EmptyResultAction getDefault()
		{
			return LOG_NULL;
		}
	}

	public abstract String getEntityName();
	public abstract <R extends JPAAbstractEntity> List<R> getAll();
	public abstract <R extends JPAAbstractEntity> R getByID(int ID, EmptyResultAction era);
	public <R extends JPAAbstractEntity> R getByID(int ID)
	{
		return getByID(ID, EmptyResultAction.LOG_NULL);
	}
	public boolean existsByID(int ID){
		return getByID(ID, EmptyResultAction.NULL)!=null;
	}
	
	public void updateEntity(JPAAbstractEntity changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAAbstractEntity item=em.find(JPAAbstractEntity.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA AbstractEntity object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	
	public void storeEntity(Object newEntity){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			em.persist(newEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't persist JPA object.",e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	
	
}
