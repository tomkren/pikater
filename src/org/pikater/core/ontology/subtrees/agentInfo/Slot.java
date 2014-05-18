package org.pikater.core.ontology.subtrees.agentInfo;

import jade.content.Concept;

public class Slot implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1146617082338754196L;

	private String dataType;
	private String slotType;

	private String description;

	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSlotType() {
		return slotType;
	}
	public void setSlotType(String slotType) {
		this.slotType = slotType;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return this.description;
	}
}
