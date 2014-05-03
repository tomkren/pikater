package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.description.ComputingAgent;


public class CrossValidation_CABox {

	public static AgentInfo get() {

		OptionInterval intervalF = new OptionInterval(new Float(1), new Float(100));
		OptionValue valueF = new OptionValue(new Float(5));

		OptionDefault optionF = new OptionDefault();
		optionF.setName("F");
		optionF.setDescription("");
		optionF.setValue( valueF );
		optionF.setInterval( intervalF );
		optionF.setList(null);

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaCA.class.getName());
		agentInfo.setOntologyClass(ComputingAgent.class.getName());
	
		agentInfo.setName("CrossValidation-Method");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("Computing agent used for training neural networks deterministic library WEKA. As a training method is used default Cross Validation WEKA method.");

		agentInfo.addOption(optionF.toOption());

		//Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
		
		return agentInfo;
	}

}