package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualFileInputBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

public class FileInput_VirtualBox {
	
	public static AgentInfo get() {

		ValueType typeIN = new ValueType(StringValue.class);
		RestrictionsForOption restrictionIN = new RestrictionsForOption();
		restrictionIN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeIN )) ));
		
		NewOption optionIN = new NewOption(
				new StringValue("inputFile.ARFF"),
				new ValueType(StringValue.class),
				"File" );
		optionIN.setDescription("File name");
		optionIN.setTypeRestrictions(restrictionIN);
		

		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_VirtualFileInputBoxProvider.class);
		agentInfo.setOntologyClass(FileDataProvider.class);
	
		agentInfo.setName("FileInput");
		agentInfo.setDescription("This box provides a data source to other boxes.");

		agentInfo.addOption(optionIN);
		agentInfo.addOutputSlot(outputSlot);

		return agentInfo;
	}

}