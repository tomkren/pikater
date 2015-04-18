/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.ontology.subtrees.metadata.attributes;

/**
 * 
 * @author Kuba
 */
public class CategoricalAttributeMetadata extends AttributeMetadata {

	private static final long serialVersionUID = 5050710635914157333L;

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

	@Override
	public String getType() {
		return "Categorical";
	}
}
