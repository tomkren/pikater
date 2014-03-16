package pikater.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity	
public class JPADataSetLO {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	//ID of the LargeObject stored in the Postgre DB, that contains
	//Use method in pikater.utility.pikaterDatabase.Database to retrieve the data based on OID
	private Long OID;
	private Long userId;
	private Long globalMetaDataId;
	private String dataSetFileName;
		
	/**	
	public void setDataSetFile(byte[] dataSetFile){
		this.dataSetFile=dataSetFile;
	}
		
	public byte[] getDataSetFile(){
		return this.dataSetFile;
	}
		**/
	public void setOID(Long OID){
		this.OID=OID;;
	}
		
	public Long getOID(){
		return this.OID;
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
	
	public void setDataSetFileName(String dataSetFileName){
		this.dataSetFileName=dataSetFileName;
	}
		
	public String getDataSetFileName(){
		return this.dataSetFileName;
	}
	
	/**
	 * Returns the generated ID of the dataset after it has persisted.
	 * @return dataset ID
	 */
	public Long getID(){
		return id;
	}
	
}
