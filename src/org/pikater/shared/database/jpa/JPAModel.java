package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPAModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String fileHash;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getFileHash() {
		return fileHash;
	}
	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}
    
}
