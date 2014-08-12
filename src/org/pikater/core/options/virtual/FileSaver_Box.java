package org.pikater.core.options.virtual;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualFileSaverBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;
import org.pikater.core.options.SlotsHelper;

public class FileSaver_Box {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_VirtualFileSaverBoxProvider.class);
		agentInfo.importOntologyClass(FileDataSaver.class);

		agentInfo.setName("FileSaver");
		agentInfo.setDescription("This box save data to Pikater database");
		
		agentInfo.setInputSlots(
				SlotsHelper.getInputSlot_FileSaver());

		return agentInfo;

	}

}
