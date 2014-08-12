package org.pikater.core.options;

import java.util.Arrays;
import java.util.List;

import org.pikater.core.CoreConstants;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;

public abstract class SlotsHelper
{
	//-------------------------------------------------------
	// INDIVIDUAL SLOT DEFINITIONS
	
	public static List<Slot> getIntputSlots_CARecSearchComplex()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setSlotType(SlotTypes.RECOMMEND_TYPE);
		recomedSlot.setDataType(CoreConstants.SLOT_RECOMMEND);
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		Slot comAgentSlot = new Slot();
		comAgentSlot.setSlotType(SlotTypes.AGENT_TYPE);
		comAgentSlot.setDataType(CoreConstants.SLOT_COMPUTATION_AGENT);
		comAgentSlot.setDescription("Data computed by an agent.");
		
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.SEARCH_TYPE);
		searchSlot.setDataType(CoreConstants.SLOT_SEARCH);
		searchSlot.setDescription("Parameters produced by search.");
		
		return Arrays.asList(
				recomedSlot,
				comAgentSlot,
				searchSlot);
	}
	
	public static List<Slot> getOutputSlots_CARecSearchComplex()
	{
		return getOutputSlots_CA();
	}
	
	public static List<Slot> getInputSlots_CA()
	{
		Slot inputTrainingSlot = new Slot();
		inputTrainingSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputTrainingSlot.setDataType(CoreConstants.SLOT_TRAINING_DATA);

		Slot inputTestingSlot = new Slot();
		inputTestingSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputTestingSlot.setDataType(CoreConstants.SLOT_TESTING_DATA);

		Slot inputValidationSlot = new Slot();
		inputValidationSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputValidationSlot.setDataType(CoreConstants.SLOT_VALIDATION_DATA);

		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.EVALUATIONMETHOD_TYPE);
		evaluationMethodSlot.setDataType(CoreConstants.SLOT_EVALUATION_METHOD);
		
		return Arrays.asList(
				inputTrainingSlot,
				inputTestingSlot,
				inputValidationSlot,
				evaluationMethodSlot
		);
	}

	public static List<Slot> getOutputSlots_CA()
	{
		Slot comAgentSlot = new Slot();
		comAgentSlot.setSlotType(SlotTypes.AGENT_TYPE);
		comAgentSlot.setDataType(CoreConstants.SLOT_COMPUTED_DATA);
		comAgentSlot.setDescription("Data computed by an agent.");
		
		return Arrays.asList(comAgentSlot);
	}

	public static List<Slot> getOutputSlots_Search()
	{
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.SEARCH_TYPE);
		searchSlot.setDataType(CoreConstants.SLOT_SEARCH);
		searchSlot.setDescription("Parameters produced by search.");
		
		return Arrays.asList(searchSlot);
	}

	public static List<Slot> getOutputSlots_Recommend()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setSlotType(SlotTypes.RECOMMEND_TYPE);
		recomedSlot.setDataType(CoreConstants.SLOT_RECOMMEND);
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		return Arrays.asList(recomedSlot);
	}
	
	public static List<Slot> getOutputSlots_EvaluationMethod()
	{
		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.EVALUATIONMETHOD_TYPE);
		evaluationMethodSlot.setDataType(CoreConstants.SLOT_EVALUATION_METHOD);
		return Arrays.asList(evaluationMethodSlot);
	}
	
	public static List<Slot> getOutputSlot_FileInput()
	{
		Slot fileInputSlot = new Slot();
		fileInputSlot.setSlotType(SlotTypes.DATA_TYPE);
		fileInputSlot.setDataType(CoreConstants.SLOT_FILE_DATA);
		return  Arrays.asList(fileInputSlot);
	}	

	public static List<Slot> getInputSlot_FileSaver()
	{
		Slot fileSaverSlot = new Slot();
		fileSaverSlot.setSlotType(SlotTypes.DATA_TYPE);
		fileSaverSlot.setDataType(CoreConstants.SLOT_FILE_DATA);
		return  Arrays.asList(fileSaverSlot);
	}

}