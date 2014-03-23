package pikater.utility.pikaterDatabase.OLD;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Transient;


/**
 * Class representing a data entry of the table containing information about users.
 *
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String login;
	private String password;
	private Long priorityMax;
	private String email;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "users";
	@Transient
	private static EntityManagerFactory factory;
	
	public User(){	}
	public User(String login,String password,Long priorityMax,String email){
		this.login=login;
		this.password=password;
		this.priorityMax=priorityMax;
		this.email=email;
		persist();
	}
	
	public String getLogin(){
		return login;
	}
	
	public String getPassword(){
		return password;
	}
	
	public Long getPriorityMax(){
		return priorityMax;
	}
	
	public String getEmail(){
		return email;
	}
	
	
	public void setLogin(String login){
		this.login=login;
		//persist();
	}
	
	public void setPassword(String password){
		this.password=password;
		//persist();
	}
	
	public void setPriorityMax(Long priorityMax){
		this.priorityMax=priorityMax;
		//persist();
	}
	
	public void setEmail(String email){
		this.email=email;
		//persist();
	}
	
	public void persist(){
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    EntityManager em = factory.createEntityManager();
	    
	    em.getTransaction().begin();
	    em.persist(this);
	    em.getTransaction().commit();

	    em.close();
	}
}
