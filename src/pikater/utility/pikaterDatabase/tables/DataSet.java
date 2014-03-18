package pikater.utility.pikaterDatabase.tables;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class DataSet {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private byte[] dataSetFile;
	private Long userId;
	private Long globalMetaDataId;

	@Transient
	private static final String PERSISTENCE_UNIT_NAME = "roles";
	@Transient
	private static EntityManagerFactory factory;
	
	
	public void setDataSetFile(byte[] dataSetFile){
		this.dataSetFile=dataSetFile;
	}
	
	public byte[] getDataSetFile(){
		return this.dataSetFile;
	}
	
	public void setUserID(Long userID){
		this.userId=userID;
	}
	
	public Long getUserID(){
		return this.userId;
	}
	
	public void setGlobalMetaDataID(Long globalMetaDataID){
		this.globalMetaDataId=globalMetaDataID;
	}
	
	public Long getGlobalMetaDataID(){
		return this.globalMetaDataId;
	}
	
}
