package vietpot.server.Database.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

import vietpot.shared.ServerClientObjects.ClientReceive.ClientInfoDTO;
import vietpot.shared.ServerClientObjects.ClientSend.AddressDTO;

@Entity
public class Client implements Serializable, EntityInterface, EntityUpdateInterface<Client>
{
	private static final long serialVersionUID = 1L;

	public enum Status
	{
		AwaitingConfirmed, Confirmed, Denied
	}

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Key ID;

	private @Column String login;
	private @Column(nullable = false) String password;
	private @Column(nullable = false) String companyICO;
	private @Column String email;
	private @Column String companyName;
	private @Column(nullable = false) List<String> phoneNumbers;
	private @Column(nullable = false) Status status;
	private @Column Date creationTime;
	private @Column Date confirmationTime;
	private @Column Date sessionTimeout;

	private @Column(nullable = false) @OneToMany(mappedBy = "client", cascade = CascadeType.ALL) List<Address> userAddresses;
	private @Column(nullable = false, updatable = false, insertable = false) @OneToMany(mappedBy = "client") List<OrderGroup> orderGroups;

	protected Client()
	{
	}

	// to be used when creating new Clients
	public Client(String login, String password, String companyICO, String email, String name,
			List<String> phoneNumbers, List<Address> userAddresses, Date creationTime)
	{
		this.login = login;
		this.password = password;
		this.companyICO = companyICO;
		this.email = email;
		this.companyName = name;
		this.creationTime = creationTime;
		this.phoneNumbers = phoneNumbers;
		this.userAddresses = userAddresses;

		this.status = Status.AwaitingConfirmed;
	}

	// to be used from spreadsheets
	public Client(Key ID, String login, String password, String companyICO, String email, String companyName,
			List<String> phoneNumbers, Status status)
	{
		this.ID = ID;
		this.login = login;
		this.password = password;
		this.companyICO = companyICO;
		this.email = email;
		this.companyName = companyName;
		this.phoneNumbers = phoneNumbers;
		this.status = status;
	}

	public Key getID()
	{
		return ID;
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getCompanyICO()
	{
		return companyICO;
	}

	public void setCompanyICO(String companyICO)
	{
		this.companyICO = companyICO;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setPhoneNumbers(List<String> phoneNumbers)
	{
		this.phoneNumbers = phoneNumbers;
	}

	public List<String> getPhoneNumbers()
	{
		return phoneNumbers;
	}

	public List<Address> getUserAddresses()
	{
		return userAddresses;
	}

	public void setUserAddresses(List<Address> userAddresses)
	{
		this.userAddresses = userAddresses;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public Date getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public List<OrderGroup> getOrderGroups()
	{
		return orderGroups;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public ClientInfoDTO toDTO(String sessionID)
	{
		List<AddressDTO> dtoAddresses = new ArrayList<AddressDTO>();
		for(Address a : getUserAddresses())
		{
			dtoAddresses.add(a.toAddressDTO());
		}
		return new ClientInfoDTO(getCompanyName(), sessionID, getPhoneNumbers(), getEmail(), getCompanyICO(),
				dtoAddresses);
	}

	@Override
	public void update(Client newerEntityVersion)
	{
		this.login = newerEntityVersion.login;
		this.password = newerEntityVersion.password;
		this.companyICO = newerEntityVersion.companyICO;
		this.companyName = newerEntityVersion.companyName;
		this.email = newerEntityVersion.email;
		this.phoneNumbers = newerEntityVersion.phoneNumbers;
		this.userAddresses = newerEntityVersion.userAddresses;
		this.status = newerEntityVersion.status;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Client other = (Client) obj;
		if(ID == null)
		{
			if(other.ID != null) return false;
		}
		else if(!ID.equals(other.ID)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Client [ID=" + ID + "]";
	}

	public Date getSessionTimeout()
	{
		return sessionTimeout;
	}

	public void setSessionTimeout(Date sessionTimeout)
	{
		this.sessionTimeout = sessionTimeout;
	}

	public Date getConfirmationTime()
	{
		return confirmationTime;
	}

	public void setConfirmationTime(Date confirmationTime)
	{
		this.confirmationTime = confirmationTime;
	}
}