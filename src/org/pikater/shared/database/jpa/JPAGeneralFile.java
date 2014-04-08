package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPAGeneralFile {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	//ID of the LargeObject stored in the Postgre DB, that contains
	//Use method in pikater.utility.pikaterDatabase.Database to retrieve the data based on OID
	private Long OID;
	private Long userId;
	private String fileName;
	private String description;
	
	
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
		
	public void setFileName(String fileName){
		this.fileName=fileName;
	}
		
	public String getFileName(){
		return this.fileName;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
		
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * Returns the generated ID of the dataset after it has persisted.
	 * @return dataset ID
	 */
	public Long getID(){
		return id;
	}
}
