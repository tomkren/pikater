package org.pikater.core.dataStructures.slots;


public class Slot {

	public enum SlotType {
		DATA_SLOT,
		AGENT_SLOT,
		SEARCHERCH_SLOT,
		RECOMMEND_SLOT
	};

	public enum SlotSex {
		PRODUCENT,
		CONSUMENT
	};

	private SlotType type;
	private SlotSex sex;

	private String description;

	private String ontologyFieldName;


	public SlotType getType() {
		return type;
	}
	public void setType(SlotType type) {
		this.type = type;
	}

	public SlotSex getSex() {
		return sex;
	}
	public void setSex(SlotSex sex) {
		this.sex = sex;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getOntologyFieldName() {
		return ontologyFieldName;
	}
	public void setOntologyField(String ontologyFieldName) {
		this.ontologyFieldName = ontologyFieldName;
	}

}
