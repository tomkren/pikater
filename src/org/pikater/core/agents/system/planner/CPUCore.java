package org.pikater.core.agents.system.planner;

import jade.core.AID;

public class CPUCore {

	private AID aID;
	private int coreID;
	
	public CPUCore (AID aID, int coreID) {
		this.aID = aID;
		this.coreID = coreID;
	}

	public AID getAID() {
		return aID;
	}

	public int getCoreID() {
		return coreID;
	}
	
}
