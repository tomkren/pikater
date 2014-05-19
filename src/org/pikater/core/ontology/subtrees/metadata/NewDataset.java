package org.pikater.core.ontology.subtrees.metadata;

import jade.content.Concept;

public class NewDataset implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014780512687910546L;

	private String internalFileName;
	private int userId;

	public String getInternalFileName() {
		return internalFileName;
	}
	public void setInternalFileName(String internalFileName) {
		this.internalFileName = internalFileName;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
