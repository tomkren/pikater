package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.slots.SimpleSlot;

public class CrossValidation_MethodBox extends LogicalBoxDescription {
	protected CrossValidation_MethodBox() {
		super(
				"CrossValidation-Method",
				ComputingAgent.class,
				"Computing agent used for training neural networks deterministic library WEKA. As a training method is used default Cross Validation WEKA method."
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);

		this.addInputSlot(SimpleSlot.getDataSlot(FileInputBox_VirtualBoxResources.data_parsedInput, "training data"));
		this.addInputSlot(SimpleSlot.getDataSlot(FileInputBox_VirtualBoxResources.data_parsedInput, "testing data"));
		this.addInputSlot(SimpleSlot.getDataSlot(FileInputBox_VirtualBoxResources.data_parsedInput, "validation data"));

		this.addOutputSlot(SimpleSlot.getDataSlot(CrossValidation_MethodBoxResources.data_computed, "data computed by this box and provided to this output slot"));
	}
}