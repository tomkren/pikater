package org.pikater.shared.database.jpa;

public enum PikaterPriviledge {
	SAVE_DATA_SET, SAVE_BOX;

	public String getDescription() {
		switch (this) {
		case SAVE_DATA_SET:
			return "Save Dataset priviledge";
		case SAVE_BOX:
			return "Save Box priviledge";
		default:
			return null;
		}
	}
}
