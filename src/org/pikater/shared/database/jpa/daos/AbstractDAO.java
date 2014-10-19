package org.pikater.shared.database.jpa.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.SortOrder;
import org.pikater.shared.logging.database.PikaterDBLogger;

/**
 * Class {@link AbstractDAO} contains general functions to manipulate with entities.
 * These general functions are used by classes, that are inherited from this class,
 * in functions specific to that particular entity.
 *  
 *
 * @param <T> class of entity, for which actions are performed.
 */
public abstract class AbstractDAO<T extends JPAAbstractEntity> {
	private Class<T> ec;

	protected AbstractDAO(Class<T> entityClass) {
		this.ec = entityClass;
	}

	public enum EmptyResultAction {
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

		public static EmptyResultAction getDefault() {
			return LOG_NULL;
		}
	}

	public abstract String getEntityName();

	/**
	 * Function retrieving all instances of that spicific entity.
	 * @return list of all entities
	 */
	public List<T> getAll() {
		return EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery(this.getEntityName() + ".getAll", ec).getResultList();
	}

	/**
	 * Function retrieving an entity based upon the primary key. Currently primary
	 * key is an integer generated at entity persistence.
	 * @param entityID of the searched entity
	 * @param era type of action, that should be performed if no entity is found
	 * @return the entity with the given ID
	 */
	public T getByID(int entityID, EmptyResultAction era) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		T item = null;
		try {
			item = em.find(ec, entityID);
		} catch (Exception t) {
			PikaterDBLogger.logThrowable("Exception while retrieveing entity based on its primary key", t);
		}

		if (item != null) {
			return item;
		} else {
			switch (era) {
			case LOG_NULL:
				PikaterDBLogger.log(Level.WARNING, "Entity not found, returning null");
				break;
			case THROW:
				throw new NoResultException();
			default:
				break;
			}
			return null;
		}
	}

	/**
	 * Function retrieving an entity based upon its ID. If no entity is found
	 * null is returnd and error message is logged. 
	 * @param ID of the searched entity 
	 * @return the entity with the given ID
	 */
	public T getByID(int ID) {
		return getByID(ID, EmptyResultAction.LOG_NULL);
	}

	/**
	 * Checks whether the entity with the given ID is available
	 * @param entityID of the entity 
	 * @return true if the entity is present
	 */
	public boolean existsByID(int entityID) {
		return getByID(entityID, EmptyResultAction.NULL) != null;
	}

	/**
	 * Updates the value of an entity with the values from the changedEntity variable.
	 * <p>
	 * This function is used due to the fact, that update operations should be done in 
	 * persistence context.
	 * <p>
	 * To simplify the update process all variables of a particular entity are updated
	 * @param changedEntity object with the new values
	 */
	public void updateEntity(T changedEntity) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			T item = em.find(ec, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		} catch (Exception e) {
			PikaterDBLogger.logThrowable("Can't update " + changedEntity.getClass().getName() + " object.", e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	/**
	 * Stores a new - not yet persisted - entity to the database.
	 * @param newEntity entity to be stored
	 */
	public void storeEntity(Object newEntity) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			em.persist(newEntity);
			em.getTransaction().commit();
		} catch (Exception e) {
			PikaterDBLogger.logThrowable("Can't persist JPA object.", e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	/**
	 * Deletes an entity from the database.
	 * @param entityToRemove Object to be removed.
	 */
	public void deleteEntity(T entityToRemove) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			JPAAbstractEntity entity = em.find(entityToRemove.getClass(), entityToRemove.getId());
			em.remove(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			PikaterDBLogger.logThrowable("Can't remove JPA object", e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	/**
	 * This function should be used to run named queries that have one integer number
	 * in result set. Usually usage of this function is call of count queries.
	 * @param queryName name of named query
	 * @return number that was query's result
	 */
	protected int getByCountQuery(String queryName) {
		return ((Long) EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery(queryName).getSingleResult()).intValue();
	}

	/**
	 * This function should be used to run named queries that have one integer number
	 * in result set and they need one parameter to be set. Usually usage of this function is call of count queries.
	 * @param queryName name of named query
	 * @param paramName name of the parameter in the query
	 * @param param value of the parameter
	 * @return number that was query's result
	 */
	protected int getByCountQuery(String queryName, String paramName, Object param) {
		return ((Long) EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery(queryName).setParameter(paramName, param).getSingleResult()).intValue();
	}

	/**
	 * This function should be used to run named queries that have one integer number
	 * in result set and they need two parameters to be set. Usually usage of this function is call of count queries.
	 * @param queryName name of named query
	 * @param paramOneName name of the first parameter
	 * @param paramOne value of the first parameter
	 * @param paramTwoName name of the second parameter
	 * @param paramTwo value of the second parameter
	 * @return number that was query's result
	 */
	protected int getByCountQuery(String queryName, String paramOneName, Object paramOne, String paramTwoName, Object paramTwo) {
		return ((Long) EntityManagerInstancesCreator.getEntityManagerInstance().createNamedQuery(queryName).setParameter(paramOneName, paramOne).setParameter(paramTwoName, paramTwo).getSingleResult())
				.intValue();
	}

	/**
	 * Runs a named query and returns a list of entities that satisfy the query
	 * @param queryName name of named query
	 * @return list of entities returned by the query
	 */
	protected List<T> getByTypedNamedQuery(String queryName) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery(queryName, ec).getResultList();
		} finally {
			em.close();
		}
	}

	/**
	 * Runs a named query that needs one parameter to be set and returns a list of entities that satisfy the query. 
	 * @param queryName name of named query
	 * @param paramName name of the parameter in query
	 * @param param value of the parameter
	 * @return list of entities returned by the query
	 */
	protected List<T> getByTypedNamedQuery(String queryName, String paramName, Object param) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery(queryName, ec).setParameter(paramName, param).getResultList();
		} finally {
			em.close();
		}
	}

	/**
	 * Runs a named query that needs one parameter to be set. 
	 * Returns a list of entities that satisfy the query starting from offset position
	 * and in maximal amount specified.
	 * @param queryName name of named query
	 * @param paramName  name of the parameter in the query
	 * @param param value of the parameter
	 * @param offset starting position of entities
	 * @param maxResultSize maximal number of entities returned
	 * @return list of entities returned by the query
	 */
	protected List<T> getByTypedNamedQuery(String queryName, String paramName, Object param, int offset, int maxResultSize) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery(queryName, ec).setParameter(paramName, param).setMaxResults(maxResultSize).setFirstResult(offset).getResultList();
		} finally {
			em.close();
		}
	}

	/**
	 * Runs a named query that needs two parameters to be set and returns a list of entities that satisfy the query.
	 * @param queryName name of named query
	 * @param paramName1 name of the first parameter
	 * @param param1 value of the first parameter
	 * @param paramName2 name of the second parameter
	 * @param param2 value of the second parameter
	 * @return list of entities returned by the query
	 */
	protected List<T> getByTypedNamedQueryDouble(String queryName, String paramName1, Object param1, String paramName2, Object param2) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery(queryName, ec).setParameter(paramName1, param1).setParameter(paramName2, param2).getResultList();
		} finally {
			em.close();
		}
	}

	/**
	 * Runs a named query that needs two parameters to be set.
	 * Returns a list of entities that satisfy the query, starting from offset position in specified maximal amount.
	 * @param queryName name of named query
	 * @param paramName1 name of the first parameter
	 * @param param1 value of the first parameter
	 * @param paramName2 name of the second parameter
	 * @param param2 value of the second parameter
	 * @param offset starting position of entities
	 * @param maxResultCount maximal number of entities returned
	 * @return list of entities returned by the query
	 */
	protected List<T> getByTypedNamedQueryDouble(String queryName, String paramName1, Object param1, String paramName2, Object param2, int offset, int maxResultCount) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery(queryName, ec).setParameter(paramName1, param1).setParameter(paramName2, param2).setFirstResult(offset).setMaxResults(maxResultCount).getResultList();
		} finally {
			em.close();
		}
	}

	/**
	 * Runs a named query and returns the first item returned by the query. If no entity is found null is returned.
	 * @param queryName name of named query
	 * @param paramName name of the parameter
	 * @param param value of the parameter
	 * @return first entity returned by the query
	 */
	protected T getSingleResultByTypedNamedQuery(String queryName, String paramName, Object param) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			return em.createNamedQuery(queryName, ec).setParameter(paramName, param).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			return null;
		} finally {
			em.close();
		}
	}

	//----------------------------------------------
	//-------Functions using Criteria API-----------
	//----------------------------------------------

	protected CriteriaBuilder getCriteriaBuilder() {
		return EntityManagerInstancesCreator.getEntityManagerInstance().getCriteriaBuilder();
	}

	private Root<T> root = null;

	/**
	 * Creates the new root element for query building specific for particular entity class.
	 * @return root element for query building
	 */
	protected Root<T> getRoot() {
		if (root == null) {
			root = getCriteriaBuilder().createQuery(ec).from(ec);
		}
		return root;
	}

	/**
	 * Creates path to entity's corresponding columns (in database representation) based upon column used in views.
	 * @param column of the table
	 * @return path to entity's corresponding variable
	 */
	protected Path<Object> convertColumnToJPAParam(ITableColumn column) {
		return getRoot().get("id");
	}

	/**
	 * Function returning all entities using criteria query.
	 * @return list of all entities
	 */
	protected List<T> getByCriteriaQuery() {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaQuery<T> query = getCriteriaBuilder().createQuery(ec);

		return em.createQuery(query.select(getRoot())).getResultList();
	}

	/**
	 * Performs a criteria query that returns entities satisfying the given predicate. 
	 * @param wherePredicate predicate that must be satisfied by the returned entities
	 * @return list of entities
	 */
	protected List<T> getByCriteriaQuery(Predicate wherePredicate) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaQuery<T> query = getCriteriaBuilder().createQuery(ec);

		return em.createQuery(query.select(getRoot()).where(wherePredicate)).getResultList();
	}

	/**
	 * Performs a criteria query that returns the number of entities satisfying the given predicate.
	 * @param wherePredicate predicate that must be satisfied by the returned entities
	 * @return number of entities satisfying the predicate
	 */
	protected int getByCriteriaQueryCount(Predicate wherePredicate) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaQuery<Long> query = getCriteriaBuilder().createQuery(Long.class);

		return em.createQuery(query.select(getCriteriaBuilder().count(getRoot())).where(wherePredicate)).getSingleResult().intValue();
	}

	/**
	 * Performs a criteria query that returns all entities from offset position in a specified maximum amount ordered according to the given column in the view.
	 * @param sortColumn column defining the order of entities
	 * @param sortOrder ascending or descending ordering
	 * @param offset start position of the entities
	 * @param maxResultCount maximum number of entities to be returned
	 * @return list of entities
	 */
	protected List<T> getByCriteriaQuery(ITableColumn sortColumn, SortOrder sortOrder, int offset, int maxResultCount) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(ec);

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

		return em.createQuery(query).setFirstResult(offset).setMaxResults(maxResultCount).getResultList();
	}

	/**
	 * Performs a criteria criteria that returns all entities satisfying the given predicate.
	 * The result list contains entities sorted according to a given column in ascending or descending order.
	 * @param wherePredicate predicate that must be satisfied by the returned entities
	 * @param sortColumn column defining the order of entities
	 * @param sortOrder ascending or descending ordering
	 * @param offset start position of entities
	 * @param maxResultCount maximum number of entities 
	 * @return list of entities
	 */
	protected List<T> getByCriteriaQuery(Predicate wherePredicate, ITableColumn sortColumn, SortOrder sortOrder, int offset, int maxResultCount) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		CriteriaBuilder cb = getCriteriaBuilder();
		CriteriaQuery<T> query = cb.createQuery(ec);

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

		return em.createQuery(query).setFirstResult(offset).setMaxResults(maxResultCount).getResultList();
	}

	/**
	 * Deletes an entity with the given ID from the database.
	 * @param id of the entity to be removed
	 */
	public void deleteEntityByID(int id) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try {
			T entity = em.find(ec, id);
			em.remove(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			PikaterDBLogger.logThrowable("Can't remove JPA object", e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

}
