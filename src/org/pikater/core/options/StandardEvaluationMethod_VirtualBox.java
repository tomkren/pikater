package org.pikater.core.options;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualBoxStandardEvaluationMethod;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;

public class StandardEvaluationMethod_VirtualBox {

	public static AgentInfo get() {
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setName("Standart Evaluation Method");
		agentInfo.setDescription("Evaluation Method");
		agentInfo.importAgentClass(Agent_VirtualBoxStandardEvaluationMethod.class);
		agentInfo.importOntologyClass(EvaluationMethod.class);

		agentInfo.setOutputSlots(
				AAA_SlotHelper.getCEvaluationMethodOutputSlots());

		return agentInfo;
	}
}
