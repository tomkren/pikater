package org.pikater.core.ontology.subtrees.batchdescription.duration;

import org.pikater.shared.util.ICloneable;

import jade.content.Concept;

public class ExpectedDuration implements Concept, ICloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7660174596901168662L;

	private String durationType;

	public ExpectedDuration() {
		this.durationType = DurationType.MINUTES.name();
	}

	public static enum DurationType {
		SECONDS("Seconds"), MINUTES("Minutes"), HOURS("Hours"), DAYS("Days"), WEEKS(
				"Weeks");

		private String type;

		DurationType(String type) {
			this.type = type;
		}

		public String getGuiValue() {
			return type;
		}

		public static DurationType getDurationType(String guiValue) {

			for (DurationType durationTypeI : values()) {
				if (durationTypeI.getGuiValue().equalsIgnoreCase(guiValue)) {
					return durationTypeI;
				}
			}
			throw new IllegalArgumentException("Argument type: " + guiValue);
		}

	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public DurationType exportDurationType() {
		return DurationType.valueOf(this.durationType);
	}

	public void importDurationType(DurationType durationType) {
		this.durationType = durationType.name();
	}

	@Override
	public ExpectedDuration clone() {
		ExpectedDuration result;
		try {
			result = (ExpectedDuration) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.setDurationType(durationType);
		return result;
	}
}
