package pikater.utility.pikaterDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Model {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private byte[] modelFile;
	private String description;
	private Long userId; 
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "roles";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setModelFile(byte[] modelFile){
		this.modelFile=modelFile;
	}
	
	public byte[] getModelFile(){
		return this.modelFile;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setUserID(Long userID){
		this.userId=userID;
	}
	
	public Long getUserID(){
		return this.userId;
	}
	
}
