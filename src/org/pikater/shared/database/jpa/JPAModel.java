package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Model")
public class JPAModel extends JPAAbstractEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@OneToOne
	private JPAResult creator;
	
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
	public JPAResult getCreator() {
		return creator;
	}
	public void setCreator(JPAResult creator) {
		this.creator = creator;
	}
	@Transient
	public static final String EntityName = "Model";
	
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPAModel updateValues=(JPAModel)newValues;
		this.fileHash=updateValues.getFileHash();
		this.creator=updateValues.getCreator();
	}
    
}
