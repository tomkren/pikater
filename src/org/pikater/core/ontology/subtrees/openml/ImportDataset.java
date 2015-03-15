package org.pikater.core.ontology.subtrees.openml;

import jade.content.AgentAction;

public class ImportDataset implements AgentAction {

	private static final long serialVersionUID = -1304110325019219428L;

	private int did = -1;

	/**
	 * Gets ID of the dataset we want to import from OpenML's repository
	 * @return dataset ID at OpenML.org
	 */
	public int getDid() {
		return did;
	}

	/**
	 * Sets ID of the dataset we want to import from OpenML's repository
	 * @param did dataset ID at OpenML.org
	 */
	public void setDid(int did) {
		this.did = did;
	}
	
	private String destination;

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}
