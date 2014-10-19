/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.ontology.subtrees.metadata.attributes;

import jade.content.Concept;

/**
 * Metadata of one attribute
 * 
 * @author Kuba
 */
public class AttributeMetadata implements Concept {

	private static final long serialVersionUID = -1009786277447730894L;

	private String name;
	private boolean isTarget;
	private double ratioOfMissingValues = 0;
	private int order;
	private double entropy = 0;
	private double attributeClassEntropy = 0;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIsTarget() {
		return isTarget;
	}

	public void setIsTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}

	public double getRatioOfMissingValues() {
		return ratioOfMissingValues;
	}

	public void setRatioOfMissingValues(double ratioOfMissingValues) {
		this.ratioOfMissingValues = ratioOfMissingValues;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public boolean getHasMissingValues() {
		return getRatioOfMissingValues() == 0;
	}

	public double getAttributeClassEntropy() {
		return attributeClassEntropy;
	}

	public void setAttributeClassEntropy(double attributeClassEntropy) {
		this.attributeClassEntropy = attributeClassEntropy;
	}

	
	public String getType() {
		return "Base";
	}
}
