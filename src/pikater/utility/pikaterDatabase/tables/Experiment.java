package pikater.utility.pikaterDatabase.tables;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Experiment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String note;
	private byte[] workflow;
	private Long batchId;
	private Long modelId;
	private String status;
	
	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "experiments";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setNote(String note){
		this.note=note;
	}
	
	public String getNote(){
		return this.note;
	}
	
	public void setWorkFlow(byte[] workflow){
		this.workflow=workflow;
	}
	
	public byte[] getWorkFlow(){
		return this.workflow;
	}
	
	public void setBatchID(Long batchID){
		this.batchId=batchID;
	}
	
	public Long getBatchID(){
		return this.batchId;
	}
	
	public void setModelID(Long modelID){
		this.modelId=modelID;
	}
	
	public Long getModelID(){
		return this.modelId;
	}
	
	public void setStatus(String status){
		this.status=status;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	
}
