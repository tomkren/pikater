package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.postgre.MyPGConnection;
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
	
	public void deleteEntity(JPAAbstractEntity entityToRemove){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAAbstractEntity entity = em.find(entityToRemove.getClass(), entityToRemove.getId());
			em.remove(entity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't remove JPA object", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	protected <T extends JPAAbstractEntity> List<T> getByTypedNamedQuery(Class<T> entityClass,String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,entityClass)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	protected <T extends JPAAbstractEntity> List<T> getByTypedNamedQuery(Class<T> entityClass,String queryName,String paramName,Object param, int offset,int maxResultSize){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,entityClass)
				.setParameter(paramName, param)
				.setMaxResults(maxResultSize)
				.setFirstResult(offset)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	
	public void deleteEntityByID(Class<? extends JPAAbstractEntity> entityClass,int id){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAAbstractEntity entity = em.find(entityClass, id);
			em.remove(entity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't remove JPA object", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	/**
	 * Test Database connectivity with testing following features
	 * 1. accessibility of Postgre connection for Large Objects
	 * 2. accessibility of entity table
	 * @return true if database is reachable
	 */
	public boolean testDatabaseConenctivity(){
		if(!MyPGConnection.isConnectionToCurrentPGDBEstablished()){
			return false;
		}
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		Query q=em.createNativeQuery("select count(*) from "+getEntityName());
		try{
			@SuppressWarnings("unused")
			Long bg = (Long) q.getSingleResult();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	
}
