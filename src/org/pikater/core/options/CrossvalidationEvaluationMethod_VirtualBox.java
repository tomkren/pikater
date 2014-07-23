package org.pikater.core.options;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;

public class CrossvalidationEvaluationMethod_VirtualBox {

	public static AgentInfo get() {
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setName("Crossvalidation Evaluation Method");
		agentInfo.setDescription("Evaluation Method");
		agentInfo.importAgentClass(CrossvalidationEvaluationMethod_VirtualBox.class);
		agentInfo.importOntologyClass(EvaluationMethod.class);
		
		agentInfo.setOutputSlots(
				AAA_SlotHelper.getCEvaluationMethodOutputSlots());

		return agentInfo;
	}
}