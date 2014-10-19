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

	public int getNumberOfCategories() {
		return numberOfCategories;
	}

	public void setNumberOfCategories(int numberOfCategories) {
		this.numberOfCategories = numberOfCategories;
	}

	@Override
	public String getType() {
		return "Categorical";
	}

}
