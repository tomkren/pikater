package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Model_20140430")
public class JPAModel extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
    private String fileHash;

	public String getFileHash() {
		return fileHash;
	}
	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}
	@Override
	public String getEntityName() {
		return "Model";
	}
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAModel updateValues=(JPAModel)newValues;
		this.fileHash=updateValues.getFileHash();
	}
    
}
