package org.pikater.core;

/**
 * 
 * Enum represents the names of system agents
 *
 */
public enum CoreAgents {
	
	INITIATOR("Initiator"),
	DATA_MANAGER("DataManager"),
	GUI_AGENTS_COMMUNICATOR("GuiAgentsCommunicator"),
	ARFF_READER("ARFFReader"),
	MANAGER_AGENT("ManagerAgent"),
	MANAGER("Manager"),
	COMPUTING_AGENT("ComputingAgent"),
	PLANNER("Planner"),
	GUI_AGENT("GUIAgent"),
	GUI_KLARA_AGENT("GUIKlaraAgent"),
	GATEWAY("Gateway"),
	AGENTINFO_MANAGER("AgentInfoManager"),
	VIRTUAL_BOX_PROVIDER("VirtualBoxProvider"),
	METADATA_QUEEN("MetadataQueen"),
	MAILING("Mailing"),
	DURATION("Duration"),
	DURATION_SERVICE("DurationServiceRegression");
	
	private final String name;
	
	private CoreAgents(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}