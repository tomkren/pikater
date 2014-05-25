package org.pikater.core.ontology.subtrees.metadata;

import jade.content.Concept;

public class NewDataset implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014780512687910546L;

	private int userId;
	private String internalFileName;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getInternalFileName() {
		return internalFileName;
	}
	public void setInternalFileName(String internalFileName) {
		this.internalFileName = internalFileName;
	}

}
