package org.pikater.core.options.evaluation;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualBoxStandardEvaluationMethod;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.options.SlotsHelper;

public class StandardEvaluationMethod_Box {

	public static AgentInfo get() {
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setName("Standart");
		agentInfo.setDescription("Evaluation Method");
		agentInfo.importAgentClass(Agent_VirtualBoxStandardEvaluationMethod.class);
		agentInfo.importOntologyClass(EvaluationMethod.class);

		agentInfo.setOutputSlots(
				SlotsHelper.getSlots_EvaluationMethodOutput());

		return agentInfo;
	}
}
