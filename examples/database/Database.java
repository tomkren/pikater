package vietpot.server.Database;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.Text;

import vietpot.server.ErrorLogger;
import vietpot.server.Database.Entities.EntityDeleteInterface;
import vietpot.server.Database.Entities.EntityInterface;
import vietpot.server.Database.Entities.EntityUpdateInterface;
import vietpot.shared.Exceptions.EntityNotFoundException;

public final class Database
{
	/**
	 * @throws IllegalStateException
	 *             - if EntityManager is a JTA EntityManager or it is container-managed
	 * @throws RollbackException
	 *             - if the commit fails
	 * @throws IllegalArgumentException
	 *             - if not an entity or if a detached entity
	 * @throws TransactionRequiredException
	 *             - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION
	 *             and there is no transaction.
	 * @return false - if entity is not found
	 * @return true - if entity is removed
	 */
	public static <E> boolean deleteHard(Class<E> eClass, Object entityID)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try
		{
			E oldEntity = em.find(eClass, entityID);
			if(oldEntity != null)
			{
				em.remove(oldEntity);
				return true;
			}
			else
			{
				return false;
			}
		}
		finally
		{
			em.getTransaction().commit();
			em.close();
		}
	}

	public static <E extends EntityDeleteInterface> boolean deleteSoft(Class<E> eClass, Object entityID)
			throws EntityNotFoundException
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try
		{
			E oldEntity = em.find(eClass, entityID);
			if(oldEntity != null)
			{
				return oldEntity.kill();
			}
			else
			{
				throw new EntityNotFoundException();
			}
		}
		finally
		{
			em.getTransaction().commit();
			em.close();
		}
	}

	/**
	 * @throws IllegalStateException
	 *             - if EntityManager is a JTA EntityManager or it is container-managed
	 * @throws RollbackException
	 *             - if the commit fails
	 * @throws IllegalArgumentException
	 *             - if the first argument does not denote an entity type or the second argument is not a valid
	 *             type for that entity's primary key
	 */
	@SuppressWarnings("unchecked")
	public static <E extends EntityInterface & EntityUpdateInterface<E>> void update(E entity)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try
		{
			E oldEntity = em.find((Class<E>) entity.getClass(), entity.getID());
			oldEntity.update(entity);
		}
		finally
		{
			em.getTransaction().commit();
			em.close();
		}
	}

	/**
	 * @throws IllegalStateException
	 *             - if EntityManager is a JTA EntityManager or it is container-managed
	 * @throws RollbackException
	 *             - if the commit fails
	 * @throws EntityExistsException
	 *             - if the entity already exists
	 * @throws IllegalArgumentException
	 *             - if not an entity
	 * @throws TransactionRequiredException
	 *             - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION
	 *             and there is no transaction
	 */
	public static void store(Object entity)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try
		{
			em.persist(entity);
		}
		finally
		{
			em.getTransaction().commit();
			em.close();
		}
	}

	public static List<String> toStrings(List<Text> texts)
	{
		List<String> result = new ArrayList<String>();
		for(Text t : texts)
		{
			result.add(t.getValue());
		}
		return result;
	}

	public static List<Text> toTexts(List<String> strings)
	{
		List<Text> result = new ArrayList<Text>();
		for(String s : strings)
		{
			result.add(new Text(s));
		}
		return result;
	}

	private static BigDecimal hundred = new BigDecimal(100);

	public static Long numberStringToDbLong(String value) throws IllegalArgumentException
	{
		BigDecimal bg = numberStringToDbBigDecimal(value);
		return bg.multiply(hundred).longValue();
	}

	public static BigDecimal numberStringToDbBigDecimal(String value) throws IllegalArgumentException
	{
		if(value.matches("[0-9]+") || value.matches("[0-9]+\\.[0-9]+"))
		{
			return new BigDecimal(value);
		}
		else if(value.matches("[0-9]+\\,[0-9]+"))
		{
			return new BigDecimal(value.replace(',', '.'));
		}
		else
		{
			IllegalArgumentException iae = new IllegalArgumentException("The parameter 's' doesn't match the "
					+ "required format.");
			ErrorLogger.logThrowable("", iae);
			throw iae;
		}
	}

	public static Double dbLongToDouble(Long l)
	{
		return new Double(l / 100.);
	}

	private static SimpleDateFormat czechianDateFormat = new SimpleDateFormat("dd.MM.yyyy");
	public static Date distantFutureTime;

	static
	{
		try
		{
			distantFutureTime = parseCzechianDate("01.01.3000");
		}
		catch(ParseException pe)
		{
			ErrorLogger.logThrowable("Could not initialize the 'distantFutureTime' variable. "
					+ "Has the date format changed?", pe);
			distantFutureTime = null;
		}
	}

	public static Date parseCzechianDate(String date) throws ParseException
	{
		Date result = czechianDateFormat.parse(date);
		return result;
	}

	public static String printCzechianDate(Date date)
	{
		return date == null ? "" : czechianDateFormat.format(date);
	}

	/*
	public static SessionOrder fromOGDTOtoSO(OrderGroupDTO ogDTO)
	{
		Map<String, List<MerchandiseDTO>> includedMerchandise = new HashMap<String, List<MerchandiseDTO>>();
		Map<String, List<Key>> usedSpecialOffers = new HashMap<String, List<Key>>();
		for(Entry<String, List<MerchandiseDTO>> entry : ogDTO.includedMerchandise.entrySet())
		{
			List<MerchandiseDTO> merchandiseBySupplier = new ArrayList<MerchandiseDTO>();
			for(MerchandiseDTO mDTO : entry.getValue())
			{
				merchandiseBySupplier.add(new MerchandiseDTO(KeyFactory.stringToKey(mDTO.getID()),
						mDTO.amount, KeyFactory.stringToKey(mDTO.chosenOffer.getOfferID())));
			}
			includedMerchandise.put(entry.getKey(), merchandiseBySupplier);
		}
		for(Entry<String, List<SpecialOfferDTO>> entry : ogDTO.usedSpecialOffers.entrySet())
		{
			List<Key> specialOffersBySupplier = new ArrayList<Key>();
			for(SpecialOfferDTO soDTO : entry.getValue())
			{
				specialOffersBySupplier.add(KeyFactory.stringToKey(soDTO.getID()));
			}
		}
		return new SessionOrder(null, ogDTO.name, includedMerchandise, usedSpecialOffers);
	}
	*/

	//The following regex recognises and removes:
	//http://www.unicode.org/charts/PDF/U0300.pdf - 'combining diacritical marks'.
	private static String removedCharsRegex = "[\\u0300-\\u036F]+";

	public static String normaliseSearchTag(String string)
	{
		string = string.toLowerCase();
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		string = string.replaceAll(removedCharsRegex, "");
		string = string.replaceAll("đ", "d");//not a diacritical mark
		return string;
	}
}
