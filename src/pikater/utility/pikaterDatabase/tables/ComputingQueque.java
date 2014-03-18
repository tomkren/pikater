package pikater.utility.pikaterDatabase.tables;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class ComputingQueque {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String parameters;
	private int experimtentID;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "computingqueues";
	@Transient
	private static EntityManagerFactory factory;
	
	
	
	public void setParameters(String parameters){
		this.parameters=parameters;
	}
	
	public String getParameters(){
		return this.parameters;
	}
	
	public void setExperimentID(int experimentID){
		this.experimtentID=experimentID;
	}
	
	public int getExperimentID(){
		return this.experimtentID;
	}
	
}
