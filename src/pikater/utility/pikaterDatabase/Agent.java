package pikater.utility.pikaterDatabase;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Agent {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private byte[] agentFile;
	private String description;
	private Long userId;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "agents";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setAgentFile(byte[] agentFile){
		this.agentFile=agentFile;
	}
	
	public byte[] getAgentFile(){
		return this.agentFile;
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
