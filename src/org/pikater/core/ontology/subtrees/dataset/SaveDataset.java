package org.pikater.core.ontology.subtrees.dataset;


import jade.content.AgentAction;

public class SaveDataset implements AgentAction {

	private static final long serialVersionUID = 8885019280601751665L;

	private int userID;
	private String description;
	private String sourceFile;
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	
}