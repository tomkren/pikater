package org.pikater.core.ontology.subtrees.metadata;

import jade.content.AgentAction;

public class SaveMetadata implements AgentAction {

	private static final long serialVersionUID = 8885019280601751665L;

	private Metadata metadata;
	private int dataSetID;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public int getDataSetID() {
		return dataSetID;
	}

	public void setDataSetID(int dataSetID) {
		this.dataSetID = dataSetID;
	}

}