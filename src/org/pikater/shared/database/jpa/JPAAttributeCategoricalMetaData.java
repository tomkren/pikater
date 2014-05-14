package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="AttributeCategoricalMetaData_20140430")
@Inheritance(strategy=InheritanceType.JOINED)
public class JPAAttributeCategoricalMetaData extends JPAAttributeMetaData{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	private int numberOfCategories;
	public int getNumberOfCategories() {
		return numberOfCategories;
	}
	public void setNumberOfCategories(int numberOfCategories) {
		this.numberOfCategories = numberOfCategories;
	}
	@Override
	public String getEntityName() {
		return "AttributeCategoricalMetaData";
	}
	
}
