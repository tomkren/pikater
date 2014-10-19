package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Class {@link JPAAttributeCategoricalMetaData} represents a record about
 * metadata of dataset's one categorical attribute.
 */
@Entity
@Table(name = "AttributeCategoricalMetaData")
@Inheritance(strategy = InheritanceType.JOINED)
public class JPAAttributeCategoricalMetaData extends JPAAttributeMetaData {

	private int numberOfCategories;

	public int getNumberOfCategories() {
		return numberOfCategories;
	}

	public void setNumberOfCategories(int numberOfCategories) {
		this.numberOfCategories = numberOfCategories;
	}

	@Transient
	public static final String EntityName = "AttributeCategoricalMetaData";

}
