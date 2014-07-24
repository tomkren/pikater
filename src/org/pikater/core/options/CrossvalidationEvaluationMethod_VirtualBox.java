package org.pikater.core.options;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class CrossvalidationEvaluationMethod_VirtualBox {

	public static AgentInfo get() {
		
		NewOption optionF = new NewOption("F", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(20))
		);
		optionF.setDescription("F");

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setName("Crossvalidation");
		agentInfo.setDescription("Evaluation Method");
		agentInfo.addOption(optionF);
		agentInfo.importAgentClass(CrossvalidationEvaluationMethod_VirtualBox.class);
		agentInfo.importOntologyClass(EvaluationMethod.class);
				
		agentInfo.setOutputSlots(
				AAA_SlotHelper.getCEvaluationMethodOutputSlots());

		return agentInfo;
	}
}