package vietpot.server.Database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import vietpot.server.Database.Entities.EntityInterface;

import com.google.appengine.api.datastore.Key;

public abstract class AbstractDAOInterface
{
	protected static final String queryNoFilter = "SELECT {0} FROM {1} x";
	protected static final String queryFieldEquality = "SELECT {0} FROM {1} x WHERE x.{2} = ?1";

	public static final String FIELD_ID = "ID";
	protected static final String FIELD_PARENT_ID = "parentId";

	protected static final String whatID = FIELD_ID;
	protected static final String whatEverything = "x";

	public abstract String getEntityName();

	// exception handling for DAO methods using the JPA technology
	/*
	 * @throws IllegalStateException - if EntityManager is a JTA EntityManager
	 * or it is container-managed.
	 * 
	 * @throws EntityExistsException - if the entity already exists.
	 * 
	 * @throws RollbackException - if the commit fails.
	 */

	public <R> R getByID(Key ID)
	{
		if(ID == null)
		{
			throw new NullPointerException();
		}
		return getSingleByAttribute(false, FIELD_ID, ID);
	}

	public boolean existsByID(Key ID)
	{
		if(ID == null)
		{
			throw new NullPointerException();
		}
		return getSingleByAttribute(true, FIELD_ID, ID) != null;
	}

	@SuppressWarnings("unchecked")
	public <R> List<R> getAll(boolean onlyFetchKeys)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			return new ArrayList<R>((List<R>) em.createQuery(
					queryNoFilter.replace("{0}", onlyFetchKeys ? whatID : whatEverything).replace("{1}",
							getEntityName())).getResultList());
		}
		finally
		{
			em.clear();
		}
	}

	/**
	 * 
	 * @param query
	 * @param params
	 * @deprecated This method is good for testing purposes but the calls to it should be replaced as soon as the
	 *             query is tested and working.
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public <R> List<R> getByQuery(String query, Object... params)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			Query q = em.createQuery(query);
			for(int i = 0; i < params.length; i++)
			{
				q.setParameter(i + 1, params[i]);
			}
			return q.getResultList();
		}
		finally
		{
			em.clear();
		}
	}

	/**
	 * @param field
	 *            - always a static String denoted as "fieldXYZ" of the corresponding DAO object (same as return
	 *            type R)
	 * @param fieldValue
	 *            - search value for the provided field of the given entity
	 * @return an R instance - if a single existent entity is found
	 * @return null - if none or more than 1 entities are found
	 */
	public <R> R getSingleByAttribute(boolean onlyFetchKey, String field, Object fieldValue)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			return getSingleByAttribute(onlyFetchKey, field, fieldValue, em);
		}
		finally
		{
			em.clear();
		}
	}

	protected <R> R getSingleByAttribute(boolean onlyFetchKey, String field, Object fieldValue, EntityManager em)
	{
		@SuppressWarnings("unchecked")
		List<R> queryResult = (List<R>) em.createQuery(
				queryFieldEquality.replace("{0}", onlyFetchKey ? whatID : whatEverything).replace("{1}",
						getEntityName()).replace("{2}", field)).setParameter(1, fieldValue).getResultList();
		if((queryResult != null) && (queryResult.size() == 1))
		{
			return queryResult.get(0);
		}
		else
		{
			return null;
		}
	}

	protected <R> List<R> getListByAttribute(boolean onlyFetchKeys, String field, Object fieldValue,
			EntityManager em)
	{
		@SuppressWarnings("unchecked")
		List<R> queryResult = (List<R>) em.createQuery(
				queryFieldEquality.replace("{0}", onlyFetchKeys ? whatID : whatEverything).replace("{1}",
						getEntityName()).replace("{2}", field)).setParameter(1, fieldValue).getResultList();
		return queryResult;
	}

	/**
	 * @param field
	 *            - always a static String denoted as "fieldXYZ" of the corresponding DAO object (same as return
	 *            type R)
	 * @param fieldValue
	 *            - search value for the provided field of the given entity
	 * @return null or a list of entities found in the database having the given fieldValue in their field variable
	 */
	public <R> List<R> getListByAttribute(boolean onlyFetchKeys, String field, Object fieldValue)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			return getListByAttribute(onlyFetchKeys, field, fieldValue, em);
		}
		finally
		{
			em.clear();
		}
	}

	/**
	 * @param field
	 *            - always a static String denoted as "fieldXYZ" of the corresponding DAO object (same as return
	 *            type R)
	 * @param fieldValue
	 *            - search value for the provided field of the given entity
	 * @return true - if an entity instance was found from the getListByAttribute call
	 * @return false - otherwise
	 */
	public <R> boolean existsByAttribute(String field, Object fieldValue)
	{
		List<R> result = getListByAttribute(true, field, fieldValue);
		return ((result != null) && (result.size() > 0));
	}

	/**
	 * @param keys
	 *            - the set of keys to exclude from the result
	 * @return all the keys of the entity 'entityName' found in the database except the ones provided in "keys"
	 *         param
	 */
	public Set<Key> getKeysSetDifference(Set<Key> keys)
	{
		List<Key> dbKeys = getAll(true);
		dbKeys.removeAll(keys);
		return new HashSet<Key>(dbKeys);
	}

	/**
	 * This method should be used when a single entity instance needs to be updated. It will return this entity
	 * wrapped in the UpdateObject class, the purpose of which is to have FinalizeUpdate() method called. Once done
	 * so, it will commit the updated entity instance and also close the EntityManager instance used to fetch this
	 * entity. Without this method, there could be no other way to close the EntityManager instance because it
	 * needs to be closed AFTER the update.
	 * 
	 * @param field
	 *            - always a static String denoted as "fieldXYZ" of the corresponding DAO object (same as return
	 *            type R)
	 * @param fieldValue
	 *            - search value for the provided field of the given entity
	 * @return null - if no or too many (>1) results are found
	 * @return the UpdateObject - otherwise
	 */
	public <O, R> UpdateObject<R> getUpdateObjectByAttribute(String field, O fieldValue)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try
		{
			R queryResult = getSingleByAttribute(false, field, fieldValue, em);
			if(queryResult != null)
			{
				return new UpdateObject<R>(em, queryResult);
			}
			else
			{
				return null;
			}
		}
		finally
		{
			em.clear();
		}
	}
	
	public <R extends EntityInterface> List<UpdateObject<R>> getUpdateObjects(List<R> entities)
	{
		if(entities == null)
		{
			return null;
		}
		else
		{
			List<UpdateObject<R>> result = new ArrayList<UpdateObject<R>>();
			for(R entity : entities)
			{
				UpdateObject<R> uo = getUpdateObjectByAttribute(FIELD_ID, entity.getID()); 
				result.add(uo);
			}
			return result;
		}
	}
	
	protected StringBuilder constructPartialWhereClause(StringBuilder inOperatorValues, String field)
	{
		StringBuilder result = new StringBuilder();
		if(inOperatorValues.length() > 0)
		{
			result.append("x.");
			result.append(field);
			result.append(" IN ");
			result.append('(');
			result.append(inOperatorValues);
			result.append(')');
		}
		return result;
	}
}
