package org.pikater.core.ontology.subtrees.file;

import jade.content.AgentAction;

/**
 * Request for a conversion of database internal filename to external one
 * or vice versa. 
 * 
 * @author stepan
 */
public class TranslateFilename implements AgentAction {

	private static final long serialVersionUID = 2577019954868509113L;

	private int userID;
	private String externalFilename;
	private String internalFilename;

	public String getInternalFilename() {
		return internalFilename;
	}

	public void setInternalFilename(String internalFilename) {
		this.internalFilename = internalFilename;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getExternalFilename() {
		return externalFilename;
	}

	public void setExternalFilename(String externalFilename) {
		this.externalFilename = externalFilename;
	}

}