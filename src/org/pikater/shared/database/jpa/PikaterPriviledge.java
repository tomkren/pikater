package org.pikater.shared.database.jpa;

public enum PikaterPriviledge {
	SAVEDATASET, SAVEBOX;

	public String getDescription() {
		switch (this) {
		case SAVEDATASET:
			return "Save Dataset priviledge";
		case SAVEBOX:
			return "Save Box priviledge";
		default:
			return null;
		}
	}
}
