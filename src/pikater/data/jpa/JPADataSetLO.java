package pikater.data.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity	
public class JPADataSetLO {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	//ID of the LargeObject stored in the Postgre DB, that contains
	//Use method in pikater.utility.pikaterDatabase.Database to retrieve the data based on OID
	private long OID;
	private JPAUser owner;
	private String description;
	private JPAGlobalMetaData globalMetaData;
	private String hash;
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setOID(Long OID){
		this.OID=OID;
	}
		
	public Long getOID(){
		return this.OID;
	}
	
	public int getID(){
		return id;
	}

	public JPAGlobalMetaData getGlobalMetaData() {
		return globalMetaData;
	}

	public void setGlobalMetaData(JPAGlobalMetaData globalMetaData) {
		this.globalMetaData = globalMetaData;
	}

	public JPAUser getOwner() {
		return owner;
	}

	public void setOwner(JPAUser owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
