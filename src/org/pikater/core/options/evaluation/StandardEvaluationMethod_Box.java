package org.pikater.core.options.evaluation;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.Standart;
import org.pikater.core.options.SlotsHelper;

public class StandardEvaluationMethod_Box {

	public static AgentInfo get() {
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setName("EM_Standard");
		agentInfo.setDescription("Evaluation Method");
		agentInfo.importAgentClass(Standart.class);
		agentInfo.importOntologyClass(EvaluationMethod.class);

		agentInfo.setOutputSlots(
				SlotsHelper.getSlots_EvaluationMethodOutput());

		return agentInfo;
	}
}
