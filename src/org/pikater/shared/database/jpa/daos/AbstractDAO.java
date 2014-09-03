package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.SortOrder;
import org.pikater.shared.logging.database.PikaterLogger;

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
	
	protected int getByCountQuery(String queryName){
			return ((Long)EntityManagerInstancesCreator
					.getEntityManagerInstance()
					.createNamedQuery(queryName)
					.getSingleResult())
					.intValue();
	}
	
	protected int getByCountQuery(String queryName, String paramName, Object param){
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery(queryName)
				.setParameter(paramName, param)
				.getSingleResult())
				.intValue();
	}
	
	protected int getByCountQuery(String queryName, String paramOneName, Object paramOne, String paramTwoName, Object paramTwo){
		return ((Long)EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery(queryName)
				.setParameter(paramOneName, paramOne)
				.setParameter(paramTwoName, paramTwo)
				.getSingleResult())
				.intValue();
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
	
	protected List<T> getByTypedNamedQueryDouble(String queryName,String paramName1,Object param1,String paramName2,Object param2){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,ec)
				.setParameter(paramName1, param1)
				.setParameter(paramName2, param2)
				.getResultList();
		}finally{
			em.close();
		}
	}	
	
	protected List<T> getByTypedNamedQueryDouble(String queryName,String paramName1,Object param1,String paramName2,Object param2, int offset, int maxResultCount){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,ec)
				.setParameter(paramName1, param1)
				.setParameter(paramName2, param2)
				.setFirstResult(offset)
				.setMaxResults(maxResultCount)
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
	
	protected CriteriaBuilder getCriteriaBuilder(){
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.getCriteriaBuilder();
	}
	
	private Root<T> root=null;
	
	protected Root<T> getRoot(){
		if(root==null){
			root=getCriteriaBuilder().createQuery(ec).from(ec);
		}
		return root;
	}
	
	protected Path<Object> convertColumnToJPAParam(ITableColumn column){
		return getRoot().get("id");
	}
	
	protected List<T> getByCriteriaQuery(){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaQuery<T> query=getCriteriaBuilder().createQuery(ec);
		
		return em
				.createQuery(
					query
					 .select(getRoot())
					 )
				.getResultList();
	}
	
	protected List<T> getByCriteriaQuery(Predicate wherePredicate){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaQuery<T> query=getCriteriaBuilder().createQuery(ec);
		
		return em
				.createQuery(
					query
					 .select(getRoot())
					 .where(wherePredicate)
					 )
				.getResultList();
	}
	
	protected int getByCriteriaQueryCount(Predicate wherePredicate){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaQuery<Long> query=getCriteriaBuilder().createQuery(Long.class);
		
		return em
				.createQuery(
					query
					 .select(getCriteriaBuilder().count(getRoot()))
					 .where(wherePredicate)
					 )
				.getSingleResult()
				.intValue();
	}
	
	protected List<T> getByCriteriaQuery(ITableColumn sortColumn, SortOrder sortOrder, int offset, int maxResultCount){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=getCriteriaBuilder();
		CriteriaQuery<T> query=cb.createQuery(ec);
		
		query = query.select(getRoot());
		
		switch (sortOrder) {
		case ASCENDING:
			query.orderBy(cb.asc(convertColumnToJPAParam(sortColumn)));
			break;
		case DESCENDING:
			query.orderBy(cb.desc(convertColumnToJPAParam(sortColumn)));
			break;
		default:
			break;
		}
		
		return em
				.createQuery(query)
				.setFirstResult(offset)
				.setMaxResults(maxResultCount)
				.getResultList();
	}
	
	protected List<T> getByCriteriaQuery(Predicate wherePredicate, ITableColumn sortColumn, SortOrder sortOrder, int offset, int maxResultCount){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb=getCriteriaBuilder();
		CriteriaQuery<T> query=cb.createQuery(ec);
		
		query = query.select(getRoot()).where(wherePredicate);
		
		switch (sortOrder) {
		case ASCENDING:
			query.orderBy(cb.asc(convertColumnToJPAParam(sortColumn)));
			break;
		case DESCENDING:
			query.orderBy(cb.desc(convertColumnToJPAParam(sortColumn)));
			break;
		default:
			break;
		}
		
		return em
				.createQuery(query)
				.setFirstResult(offset)
				.setMaxResults(maxResultCount)
				.getResultList();
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
