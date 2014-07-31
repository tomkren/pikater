package org.pikater.core.options.virtual;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualCARecSearchComplexBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.CARecSearchComplex;
import org.pikater.core.options.SlotsHelper;

public class CARecSearchComplex_Box {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo
				.importAgentClass(Agent_VirtualCARecSearchComplexBoxProvider.class);
		agentInfo.importOntologyClass(CARecSearchComplex.class);

		agentInfo.setName("Complex");
		agentInfo.setDescription("Complex Box");

		agentInfo.addInputSlot(SlotsHelper.getSlot_ComputedData());
		agentInfo.addInputSlot(SlotsHelper.getSlot_RecommendedAgent());
		agentInfo.addInputSlot(SlotsHelper.getSlot_ParametersProducedBySearch());

		agentInfo.addOutputSlot(SlotsHelper.getSlot_ComputedData());

		return agentInfo;
	}

}
