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
	private double chiSquare;
	private double chiSquareTest;
	private double gTest;

	public int getNumberOfCategories() {
		return numberOfCategories;
	}

	public void setNumberOfCategories(int numberOfCategories) {
		this.numberOfCategories = numberOfCategories;
	}

	public double getChiSquare() {
		return chiSquare;
	}

	public void setChiSquare(double chiSquare) {
		this.chiSquare = chiSquare;
	}

	public double getChiSquareTest() {
		return chiSquareTest;
	}

	public void setChiSquareTest(double chiSquareTest) {
		this.chiSquareTest = chiSquareTest;
	}

	public double getGTest() {
		return gTest;
	}

	public void setGTest(double gTest) {
		this.gTest = gTest;
	}

	@Transient
	public static final String EntityName = "AttributeCategoricalMetaData";

}
