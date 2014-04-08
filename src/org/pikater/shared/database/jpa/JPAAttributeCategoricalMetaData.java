package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPAAttributeCategoricalMetaData {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int numberOfCategories;
	public int getNumberOfCategories() {
		return numberOfCategories;
	}
	public void setNumberOfCategories(int numberOfCategories) {
		this.numberOfCategories = numberOfCategories;
	}
	public int getId() {
		return id;
	}
	
}
