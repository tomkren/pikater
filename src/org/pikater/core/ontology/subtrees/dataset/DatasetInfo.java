package org.pikater.core.ontology.subtrees.dataset;

import jade.content.Concept;

public class DatasetInfo implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1757427945634445609L;
	
	private int datasetID;
	private String hash;
	private String fileName;
	
	public int getDatasetID() {
		return datasetID;
	}
	public void setDatasetID(int datasetID) {
		this.datasetID = datasetID;
	}
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
