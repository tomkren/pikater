package org.pikater.core.ontology.subtrees.management;

import jade.content.Concept;

public class ComputerInfo implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5958064636608966628L;

	private String operationSystem;
	private int numberOfCores;

	public String getOperationSystem() {
		return operationSystem;
	}

	public void setOperationSystem(String operationSystem) {
		this.operationSystem = operationSystem;
	}

	public int getNumberOfCores() {
		return numberOfCores;
	}

	public void setNumberOfCores(int numberOfCores) {
		this.numberOfCores = numberOfCores;
	}

}
