package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.StringValue;

public class FileInput_VirtualBox {
	
	public static AgentInfo get() {

		Type typeIN = new Type(StringValue.class);
		PossibleTypesRestriction restrictionIN = new PossibleTypesRestriction();
		restrictionIN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeIN )) ));
		
		NewOption optionIN = new NewOption(
				new StringValue("inputFile.ARFF"),
				new Type(StringValue.class),
				"File" );
		optionIN.setDescription("File name");
		optionIN.setPossibleTypesRestriction(restrictionIN);
		

		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_VirtualBoxProvider.class);
		agentInfo.setOntologyClass(FileDataProvider.class);
	
		agentInfo.setName("FileInput");
		agentInfo.setDescription("This box provides a data source to other boxes.");

		agentInfo.addOption(optionIN);
		agentInfo.addOutputSlot(outputSlot);

		return agentInfo;
	}

}