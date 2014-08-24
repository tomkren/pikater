package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.utilities.logging.PikaterLogger;

public abstract class AbstractDAO<T extends JPAAbstractEntity>
{
	private Class<T> ec;
	
	protected AbstractDAO(Class<T> entityClass){
		this.ec=entityClass;
	}
	
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
	
	public List<T> getAll(){
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery(this.getEntityName()+".getAll", ec)
				.getResultList();
	}
	
	
	
	public T getByID(int ID, EmptyResultAction era){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		T item = null;
		try{
			item=em.find(ec, ID);
		}catch(Throwable t){
			logger.log(Level.ERROR, "Exception while retrieveing entity based on its primary key", t);
		}
		
		if(item!=null){
			return item;
		}else{
			switch (era) {
			case LOG_NULL:
				logger.log(Level.WARN, "Entity not found, returning null");
				break;
			case THROW:
				throw new NoResultException();
			default:
				break;
			}
			return null;
		}
	}
	
	public T getByID(int ID)
	{
		return getByID(ID, EmptyResultAction.LOG_NULL);
	}
	
	public boolean existsByID(int ID){
		return getByID(ID, EmptyResultAction.NULL)!=null;
	}
	
	
	public void updateEntity(T changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			T item=em.find(ec, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update "+changedEntity.getClass().getName()+" object.", e);
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
	
	public void deleteEntity(T entityToRemove){
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
	
	protected List<T> getByTypedNamedQuery(String queryName){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,ec)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	protected List<T> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,ec)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	protected List<T> getByTypedNamedQuery(String queryName,String paramName,Object param, int offset,int maxResultSize){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,ec)
				.setParameter(paramName, param)
				.setMaxResults(maxResultSize)
				.setFirstResult(offset)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
	protected List<JPAFilemapping> getByTypedNamedQueryDouble(String queryName,String paramName1,Object param1,String paramName2,Object param2){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAFilemapping.class)
				.setParameter(paramName1, param1)
				.setParameter(paramName2, param2)
				.getResultList();
		}finally{
			em.close();
		}
	}	
	
	protected T getSingleResultByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return
				em
				 .createNamedQuery(queryName,ec)
				 .setParameter(paramName, param)
				 .setMaxResults(1)
				 .getSingleResult();
		}catch(Exception e){
			return null;
		}finally{
			em.close();
		}
	}
	
	public void deleteEntityByID(int id){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			T entity = em.find(ec, id);
			em.remove(entity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't remove JPA object", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
}
