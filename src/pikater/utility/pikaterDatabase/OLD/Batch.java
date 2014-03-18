package pikater.utility.pikaterDatabase.OLD;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Batch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private Long ownerId;
	private String xmlDescription;
	private Long priority;
	private Long totalPriority;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "batches";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setOwnerID(Long ownerID){
		this.ownerId=ownerID;
	}
	
	public Long getOwnerID(){
		return this.ownerId;
	}
	
	public void setXMLDescription(String xmlDescription){
		this.xmlDescription=xmlDescription;
	}
	
	public String getXMLDescription(){
		return this.xmlDescription;
	}
	
	public void setPriority(Long priority){
		this.priority=priority;
	}
	
	public Long getPriority(){
		return this.priority;
	}
	
	public void setTotalPriority(Long totalPriority){
		this.totalPriority=totalPriority;
	}
	
	public Long getTotalPriority(){
		return this.totalPriority;
	}
}
