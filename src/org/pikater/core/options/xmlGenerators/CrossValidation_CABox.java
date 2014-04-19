package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;


public class CrossValidation_CABox extends LogicalBoxDescription {
	protected CrossValidation_CABox() {
		super(
				"CrossValidation-Method",
				ComputingAgent.class,
				"Computing agent used for training neural networks deterministic library WEKA. As a training method is used default Cross Validation WEKA method."
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);

		OptionInterval intervalF = new OptionInterval(new Float(1), new Float(100));
		OptionValue valueF = new OptionValue(new Float(5));
		
		OptionDefault optionF = new OptionDefault();
		optionF.setName("F");
		optionF.setDescription("");
		optionF.setValue( valueF );
		optionF.setInterval( intervalF );
		optionF.setList(null);
		
		this.addOption(optionF);


		// Slots Definition
		this.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		this.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
	}
}