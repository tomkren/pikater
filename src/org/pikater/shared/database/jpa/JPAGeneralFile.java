package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="GeneralFile")
public class JPAGeneralFile extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	//ID of the LargeObject stored in the Postgre DB, that contains
	//Use method in pikater.utility.pikaterDatabase.Database to retrieve the data based on OID
	private long OID;
	private JPAUser user;
	private String fileName;
	private String description;
	
	
	public void setOID(long OID){
		this.OID=OID;;
	}
		
	public long getOID(){
		return this.OID;
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
	public JPAUser getUser() {
		return user;
	}

	public void setUser(JPAUser user) {
		this.user = user;
	}
	@Transient
	public static final String EntityName = "GeneralFile";

	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAGeneralFile updateValues=(JPAGeneralFile)newValues;
		this.description=updateValues.getDescription();
		this.fileName=updateValues.getFileName();
		this.OID=updateValues.getOID();
		//this.user=updateValues.getUser();
	}
}
