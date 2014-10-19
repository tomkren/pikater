package org.pikater.core.options.evaluation;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualBoxCrossvalidationEvaluationMethod;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchdescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.options.SlotsHelper;

public class CrossvalidationEvaluationMethod_Box {

	public static AgentInfo get() {
		
		NewOption optionF = new NewOption("F", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(20))
		);
		optionF.setDescription("F");

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setName("EM_XValidation");
		agentInfo.setDescription("Evaluation Method");
		agentInfo.addOption(optionF);
		agentInfo.importAgentClass(Agent_VirtualBoxCrossvalidationEvaluationMethod.class);
		agentInfo.importOntologyClass(EvaluationMethod.class);
				
		agentInfo.setOutputSlots(
				SlotsHelper.getOutputSlots_EvaluationMethod());

		return agentInfo;
	}
}