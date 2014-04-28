package vietpot.server.Database.DataAccessObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import vietpot.server.ErrorLogger;
import vietpot.server.Database.AbstractDAOInterface;
import vietpot.server.Database.EntityManagerInstancesCreator;
import vietpot.server.Database.LimitedQueryResult;
import vietpot.server.Database.Entities.Attribute;
import vietpot.server.Database.Entities.EntityInterface;
import vietpot.server.Database.Entities.Merchandise;
import vietpot.shared.ServerClientObjects.ClientSend.FiltersDTO;
import vietpot.shared.lib.MutableInteger;
import vietpot.shared.Variables.MerchandiseSellType;

public class MerchandiseDAO extends AbstractDAOInterface
{
	public static final String FIELD_ANCESTORS = "ancestors";
	public static final String FIELD_CATEGORIES = "categories";
	public static final String FIELD_POPULARITY = "popularity";
	public static final String FIELD_SUPPLIERS = "suppliers";
	public static final String FIELD_ATTRIBUTE_VALUES = "attrValues";
	public static final String FIELD_VISIBILITY = "visibility";
	public static final String FIELD_SELL_TYPES = "discountTypes";

	// "result entity-count" limited queries
	private final String QUERY_GET_MOST_POPULAR = ("SELECT {0} FROM {1} x WHERE x.{2} <> \"Dead\" ORDER BY x.{2}, x.{3} DESC")
			.replace("{0}", whatEverything)
			.replace("{1}", getEntityName())
			.replace("{2}", FIELD_VISIBILITY)
			.replace("{3}", FIELD_POPULARITY);
	
	private final String QUERY_GET_LEAST_POPULAR = ("SELECT {0} FROM {1} x WHERE x.{2} <> \"Dead\" ORDER BY x.{2}, x.{3} ASC")
			.replace("{0}", whatEverything)
			.replace("{1}", getEntityName())
			.replace("{2}", FIELD_VISIBILITY)
			.replace("{3}", FIELD_POPULARITY);
	
	private final String QUERY_GET_BY_FILTERS = ("SELECT {0} FROM {1} x WHERE x.{2} <> \"Dead\" {3}")
			.replace("{0}", whatEverything)
			.replace("{1}", getEntityName())
			.replace("{2}", FIELD_VISIBILITY);
	
	// get all queries - used by the server tasks and routines only
	private final String QUERY_GET_VISIBLE_MERCHANDISE_KEYS_BY_ANCESTOR = ("SELECT x.{0} FROM {1} x WHERE "
			+ "x.{2} = \"Visible\" AND x.{3} = ?1")
				.replace("{0}", FIELD_ID)
				.replace("{1}", getEntityName())
				.replace("{2}", FIELD_VISIBILITY)
				.replace("{3}", FIELD_ANCESTORS);
	
	private final String QUERY_GET_LIVE_MERCHANDISE_KEYS_BY_CATEGORY = ("SELECT x.{0} FROM {1} x WHERE "
			+ "x.{2} <> \"Dead\" AND x.{3} = ?1")
				.replace("{0}", FIELD_ID)
				.replace("{1}", getEntityName())
				.replace("{2}", FIELD_VISIBILITY)
				.replace("{3}", FIELD_CATEGORIES);
	
	private final String QUERY_GET_LIVE_ATTR_VALUES_BY_NAME = ("SELECT x.{0} FROM {1} x WHERE x.{0} LIKE ?1")
			.replace("{0}", FIELD_ATTRIBUTE_VALUES)
			.replace("{1}", getEntityName());

	@Override
	public String getEntityName()
	{
		return "Merchandise";
	}

	public LimitedQueryResult<Merchandise> getMostPopular(int startIndex, int endIndex)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			@SuppressWarnings("unchecked")
			List<Merchandise> queryResult = (List<Merchandise>) em.createQuery(QUERY_GET_MOST_POPULAR)
					.getResultList();
			return getLimitedResult(queryResult, startIndex, endIndex);
		}
		finally
		{
			em.clear();
		}
	}

	public LimitedQueryResult<Merchandise> getLeastPopular(int startIndex, int endIndex)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			@SuppressWarnings("unchecked")
			List<Merchandise> queryResult = (List<Merchandise>) em.createQuery(QUERY_GET_LEAST_POPULAR)
					.getResultList();
			return getLimitedResult(queryResult, startIndex, endIndex);
		}
		finally
		{
			em.clear();
		}
	}

	public LimitedQueryResult<Merchandise> getByFilters(FiltersDTO filters, int startIndex, int endIndex)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			List<Key> params = new ArrayList<Key>();
			Query query = em.createQuery(QUERY_GET_BY_FILTERS.replace("{3}", 
					" AND " + constructWholeWhereClauseByFilters(filters, params)));
			setFiltersQueryParameters(query, params);
			@SuppressWarnings("unchecked")
			List<Merchandise> queryResult = (List<Merchandise>) query.getResultList();
			return getLimitedResult(queryResult, startIndex, endIndex);
		}
		finally
		{
			em.clear();
		}
	}
	
	private <R> LimitedQueryResult<R> getLimitedResult(List<R> resultEntities, int startIndex, int endIndex)
	{
		if(checkResultWithIndexes(resultEntities, startIndex, endIndex))
		{
			if(endIndex > resultEntities.size())
			{
				endIndex = resultEntities.size();
			}
			List<R> result = resultEntities.subList(startIndex, endIndex);
			return new LimitedQueryResult<R>(result, startIndex + result.size(), new Date());
		}
		else
		{
			return null;
		}
	}
	
	private <R> boolean checkResultWithIndexes(List<R> resultEntites, int startIndex, int endIndex)
	{
		if((startIndex < 0) || (startIndex > resultEntites.size()))
		{
			return false;
		}
		if(startIndex < 0)
		{
			return false;
		}
		return true;
	}
	
	// IMPORTANT: note that this method sets parameters from the number 2 !!!
	private void setFiltersQueryParameters(Query query, List<Key> params)
	{
		int paramNumber = 2;
		for(Key key : params)
		{
			query.setParameter(paramNumber, key);
			paramNumber++;
		}
	}
	
	// IMPORTANT: note that this method sets parameters from the number 2 !!!
	private String constructWholeWhereClauseByFilters(FiltersDTO filters, List<Key> params)
	{
		StringBuilder result = new StringBuilder();
		if(filters != null)
		{
			MutableInteger paramNumber = new MutableInteger(2);
			StringBuilder categoriesResult = constructPartialWhereClause(constructWhereSomethingClauseByFilter(
					DAOs.category, paramNumber, filters.filterCategories, params), FIELD_ANCESTORS);
			StringBuilder suppliersResult = constructPartialWhereClause(constructWhereSomethingClauseByFilter(
					DAOs.supplier, paramNumber, filters.filterSuppliers, params), FIELD_SUPPLIERS);
			StringBuilder attributesResult = constructPartialWhereClause(
					constructWhereAttributeClauseByFilter(filters), FIELD_ATTRIBUTE_VALUES);
			StringBuilder sellTypesResult = constructPartialWhereClause(
					constructWhereSellTypeClauseByFilter(filters), FIELD_SELL_TYPES);
	
			result.append(categoriesResult);
			if(suppliersResult.length() > 0)
			{
				if(result.length() > 0)
				{
					result.append(" AND ");
				}
				result.append(suppliersResult);
			}
			if(attributesResult.length() > 0)
			{
				if(result.length() > 0)
				{
					result.append(" AND ");
				}
				result.append(attributesResult);
			}
			if(sellTypesResult.length() > 0)
			{
				if(result.length() > 0)
				{
					result.append(" AND ");
				}
				result.append(sellTypesResult);
			}
		}
		return result.toString();
	}

	private StringBuilder constructWhereSomethingClauseByFilter(AbstractDAOInterface dao, MutableInteger parameterNumber,
			Set<String> filters, List<Key> params)
	{
		StringBuilder result = new StringBuilder();
		if(filters != null)
		{
			for(String sKey : filters)
			{
				ErrorLogger.logWarning("getMerchandiseByFilters: Filter key: " + sKey);
				Key key = KeyFactory.stringToKey(sKey);
				EntityInterface c = dao.getByID(key);
				if(c != null)
				{
					if(result.length() != 0)
					{
						result.append(',');
					}
					result.append("?");
					result.append(parameterNumber.number);
					parameterNumber.number++;
					params.add(c.getID());
				}
			}
		}
		return result;
	}

	private StringBuilder constructWhereAttributeClauseByFilter(FiltersDTO filters)
	{
		StringBuilder result = new StringBuilder();
		if(filters.filterAttributes != null)
		{
			for(String key : filters.filterAttributes.keySet())
			{
				Key aID = KeyFactory.stringToKey(key);
				Attribute a = DAOs.attribute.getByID(aID);
				if(a != null)
				{
					for(String localizedAttrValue : filters.filterAttributes.get(key))
					{
						if(result.length() != 0)
						{
							result.append(',');
						}
						result.append('\'');
						result.append(a.getInternalName());
						result.append('=');
						result.append(localizedAttrValue);
						result.append('\'');
					}
				}
			}
		}
		return result;
	}

	private StringBuilder constructWhereSellTypeClauseByFilter(FiltersDTO filters)
	{
		StringBuilder result = new StringBuilder();
		if(filters.filterSellingTypes != null)
		{
			for(MerchandiseSellType sellType : filters.filterSellingTypes)
			{
				if(result.length() != 0)
				{
					result.append(',');
				}
				result.append('\'');
				result.append(sellType.name());
				result.append('\'');
			}
		}
		return result;
	}
	
	public List<Key> getLiveMerchandiseKeysByCategory(Key category)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			@SuppressWarnings("unchecked")
			List<Key> queryResult = em.createQuery(QUERY_GET_LIVE_MERCHANDISE_KEYS_BY_CATEGORY)
					.setParameter(1, category)
					.getResultList();
			return queryResult;
		}
		finally
		{
			em.clear();
		}
	}

	public List<List<String>> getAttrValuesByAttribute(String internalAttributeName)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			@SuppressWarnings("unchecked")
			List<List<String>> attrValues = (List<List<String>>) em.createQuery(QUERY_GET_LIVE_ATTR_VALUES_BY_NAME)
					.setParameter(1, internalAttributeName + '%')
					.getResultList();
			if(attrValues != null)
			{
				if(attrValues.size() == 0)
				{
					return null;
				}
			}
			return attrValues;
		}
		finally
		{
			em.clear();
		}
	}
	
	public List<Key> getVisibleMerchandiseKeysByAncestor(Key category)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			@SuppressWarnings("unchecked")
			List<Key> queryResult = em.createQuery(QUERY_GET_VISIBLE_MERCHANDISE_KEYS_BY_ANCESTOR)
					.setParameter(1, category)
					.getResultList();
			return queryResult;
		}
		finally
		{
			em.clear();
		}
	}
}