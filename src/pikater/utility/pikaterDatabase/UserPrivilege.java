package pikater.utility.pikaterDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class UserPrivilege {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "roles";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
}
