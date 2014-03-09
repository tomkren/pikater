package pikater.utility.pikaterDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class GlobalMetaData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long defaultTaskTypeId;
	private Long numberofInstances;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "roles";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setDefaultTaskID(Long defaultTaskID){
		this.defaultTaskTypeId=defaultTaskID;
	}
	
	public Long getDefaultTaskID(){
		return this.defaultTaskTypeId;
	}
	
	public void setNumberOfInstances(Long numberOfInstances){
		this.numberofInstances=numberOfInstances;
	}
	
	public Long getNumberOfInstances(){
		return this.numberofInstances;
	}
	
}
