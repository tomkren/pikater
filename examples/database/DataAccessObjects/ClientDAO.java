package vietpot.server.Database.DataAccessObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import vietpot.server.ErrorLogger;
import vietpot.server.FieldVerifier;
import vietpot.server.Database.AbstractDAOInterface;
import vietpot.server.Database.Database;
import vietpot.server.Database.EntityManagerInstancesCreator;
import vietpot.server.Database.UpdateObject;
import vietpot.server.Database.Entities.Address;
import vietpot.server.Database.Entities.Client;
import vietpot.server.RPCs.ManageAccountServiceImpl;
import vietpot.server.SessionManagement.SessionHandler;
import vietpot.shared.Exceptions.EntityNotFoundException;
import vietpot.shared.Exceptions.ServerErrorException;
import vietpot.shared.ServerClientObjects.ClientReceive.AccountRegisterUpdateDTO;
import vietpot.shared.ServerClientObjects.ClientReceive.AccountRegisterUpdateDTO.AddressField;
import vietpot.shared.ServerClientObjects.ClientReceive.AccountRegisterUpdateDTO.DuplicateField;
import vietpot.shared.ServerClientObjects.ClientReceive.AccountRegisterUpdateDTO.InvalidField;
import vietpot.shared.ServerClientObjects.ClientSend.AddressDTO;
import vietpot.shared.ServerClientObjects.ClientSend.ClientDTO;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ClientDAO extends AbstractDAOInterface
{
	public final String FIELD_LOGIN = "login";
	public final String FIELD_EMAIL = "email";
	public final String FIELD_ICO = "companyICO";
	public final String FIELD_PHONE_NUMBER = "phoneNumbers";
	public final String FIELD_PASSWORD = "password";
	public final String FIELD_REGISTER_COMPLETE = "registrationComplete";
	
	public final String QUERY_GET_BY_PHONENUMBER_PASSWORD = ("SELECT {0} FROM {1} x WHERE x.{2} = ?1 AND x.{3} = ?2")
			.replace("{0}", whatEverything)
			.replace("{1}", getEntityName())
			.replace("{2}", FIELD_PHONE_NUMBER)
			.replace("{3}", FIELD_PASSWORD);

	@Override
	public String getEntityName()
	{
		return "Client";
	}
	
	public List<Client> getUnverified()
	{
		return getListByAttribute(false, FIELD_REGISTER_COMPLETE, new Boolean(false));
	}
	
	// TODO: combine getByLogin and getByPhoneNumber when David decides it's time to let users have their own logins
	public List<Client> getByLogin(String login)
	{
		return getListByAttribute(false, FIELD_LOGIN, login);
	}

	public List<Client> getByPhoneNumber(String phoneNumber)
	{
		return getListByAttribute(false, FIELD_PHONE_NUMBER, phoneNumber);
	}
	
	public List<Client> getByPhoneNumberPassword(String phoneNumber, String passwordNonHashed)
	{
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try
		{
			@SuppressWarnings("unchecked")
			List<Client> queryResult = (List<Client>) em.createQuery(QUERY_GET_BY_PHONENUMBER_PASSWORD)
					.setParameter(1, phoneNumber)
					.setParameter(2, ManageAccountServiceImpl.hashPw(passwordNonHashed))
					.getResultList();
			return queryResult;
		}
		finally
		{
			em.clear();
		}
	}
	
	public Client getNewGuestClient() throws ServerErrorException
	{
		try
		{
			Date now = new Date();
			Client c = new Client(null, "", "", null, "", new ArrayList<String>(), new ArrayList<Address>(), now);
			c.setSessionTimeout(new Date(now.getTime() + SessionHandler.aDayInSeconds * 1000 + 5000));
			Database.store(c);
			return c;
		}
		catch(Exception e)
		{
			ErrorLogger.logThrowable("Could not create a new guest client because:", e);
			throw new ServerErrorException();
		}
	}
	
	public AccountRegisterUpdateDTO update(ClientDTO updatedClient) throws EntityNotFoundException
	{
		AccountRegisterUpdateDTO result = new AccountRegisterUpdateDTO();

		UpdateObject<Client> uoC = DAOs.client.getUpdateObjectByAttribute(AbstractDAOInterface.FIELD_ID, 
				KeyFactory.stringToKey(updatedClient.getID()));
		if(uoC == null)
		{
			// this shouldn't happen but still...
			throw new EntityNotFoundException();
		}

		String companyName = parseAndCheckClientInformation(updatedClient, result);

		// addresses
		List<Address> oldAddresses = uoC.entity.getUserAddresses();
		List<Address> newAddresses = new ArrayList<Address>();
		for(int i = 0; i < updatedClient.adresses.size(); i++)
		{
			AddressDTO address = updatedClient.adresses.get(i);
			parseAndCheckAddressInformation(address, i, result);
			Key addressID = KeyFactory.stringToKey(address.getID());
			if(address.getID() != null)
			{
				boolean found = false;
				for(Address a : oldAddresses)
				{
					if(a.getID().equals(addressID))
					{
						found = true;
						a.setCity(address.city);
						a.setCountry(address.country);
						a.setPostalCode(address.postalCode);
						a.setStreet(address.street);
						newAddresses.add(a);
						oldAddresses.remove(a);
						break;
					}
				}
				if(!found)
				{
					result.invalidAddressFields.put(i, AddressField.ID);
				}
			}
			else
			{
				newAddresses.add(new Address(addressID, address.city, address.country, address.street,
						address.postalCode, new GeoPt(address.geoLat, address.geoLong),
						AddressDAO.getLiveUsage()));
			}
		}

		if(!result.anyProblemEncountered())
		{
			// update client
			uoC.entity.setLogin(updatedClient.login);
			uoC.entity.setPassword(updatedClient.password);
			uoC.entity.setCompanyICO(updatedClient.companyICO);
			uoC.entity.setCompanyName(companyName);
			uoC.entity.setEmail(updatedClient.email);
			uoC.entity.setPhoneNumbers(updatedClient.phoneNumbers);
			
			for(Address a : oldAddresses)
			{
				a.setUse(AddressDAO.getNoUsage());
			}
			oldAddresses.addAll(newAddresses);
			
			// this ain't necessary because the old list is passed into this one before
			// however why not prevent a possible bug later...
			uoC.entity.setUserAddresses(oldAddresses);
			uoC.finalizeUpdate();
		}
		return result;
	}

	public AccountRegisterUpdateDTO store(ClientDTO newClientDTO)
	{
		AccountRegisterUpdateDTO result = new AccountRegisterUpdateDTO();

		String companyName = parseAndCheckClientInformation(newClientDTO, result);

		List<Address> addresses = new ArrayList<Address>();
		for(int i = 0; i < newClientDTO.adresses.size(); i++)
		{
			AddressDTO address = newClientDTO.adresses.get(i);
			parseAndCheckAddressInformation(address, i, result);
			addresses.add(new Address(address.city, address.country, address.street, address.postalCode,
					new GeoPt(address.geoLat, address.geoLong), AddressDAO.getLiveUsage()));
		}

		if(!result.anyProblemEncountered())
		{
			Client newClient = new Client(newClientDTO.login,
					ManageAccountServiceImpl.hashPw(newClientDTO.password),
					newClientDTO.companyICO,
					newClientDTO.email,
					companyName,
					newClientDTO.phoneNumbers,
					addresses, new Date());
			Database.store(newClient);
		}
		return result;
	}

	// returns company name parsed from ICO
	private String parseAndCheckClientInformation(ClientDTO client, AccountRegisterUpdateDTO result)
	{
		// Login
		if(FieldVerifier.isValidClientLogin(client.login, true))
		{
			List<Client> clients = getByLogin(client.login);
			if(clients != null)
			{
				clients.remove(client);
				if(clients.size() > 0)
				{
					result.duplicateFields.add(DuplicateField.Login);
				}
			}
		}
		else
		{
			result.invalidFields.add(InvalidField.Login);
		}

		// password
		if(!FieldVerifier.isValidClientPassword(client.password))
		{
			result.invalidFields.add(InvalidField.Password);
		}

		// ICO
		client.companyICO = FieldVerifier.removeWhiteSpaces(client.companyICO);
		String companyName = FieldVerifier.isValidClientICO(client.companyICO);
		if((companyName == null) || companyName.isEmpty())
		{
			// if companyName.isEmpty() - ICO doesn't exist
			result.invalidFields.add(InvalidField.ICO);
		}

		// email
		if(!FieldVerifier.isValidClientEmail(client.email, true))
		{
			result.invalidFields.add(InvalidField.Email);
		}

		// phone numbers
		for(int i = 0; i < client.phoneNumbers.size(); i++)
		{
			String phoneNumber = FieldVerifier.removeWhiteSpaces(client.phoneNumbers.get(i));
			if(FieldVerifier.isValidPhoneNumber(phoneNumber))
			{
				/*
				if(!result.invalidFields.contains(InvalidField.Password))
				{
					List<Client> sameClients = getByPhoneNumberPassword(phoneNumber, client.password);
					if((sameClients != null) && (!sameClients.isEmpty()))
					{
						result.duplicatePhoneNumbers.add(i);
					}
				}
				*/
				if(DAOs.client.existsByAttribute(DAOs.client.FIELD_PHONE_NUMBER, phoneNumber))
				{
					result.duplicatePhoneNumbers.add(i);
				}
				else
				{
					client.phoneNumbers.set(i, phoneNumber);
				}
			}
			else
			{
				result.invalidPhoneNumbers.add(i);
			}
		}
		
		return companyName;
	}

	private void parseAndCheckAddressInformation(AddressDTO address, int index, AccountRegisterUpdateDTO result)
	{
		if(!FieldVerifier.isValidCity(address.city))
		{
			result.invalidAddressFields.put(index, AddressField.City);
		}
		if(!FieldVerifier.isValidCountry(address.country))
		{
			result.invalidAddressFields.put(index, AddressField.Country);
		}
		address.postalCode = FieldVerifier.removeWhiteSpaces(address.postalCode);
		if(!FieldVerifier.isValidPostalCode(address.postalCode))
		{
			result.invalidAddressFields.put(index, AddressField.PostalCode);
		}
		if(!FieldVerifier.isValidStreet(address.street))
		{
			result.invalidAddressFields.put(index, AddressField.Street);
		}
	}
}