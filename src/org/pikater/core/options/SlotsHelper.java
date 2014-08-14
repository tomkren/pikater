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
		recomedSlot.setSlotType(SlotTypes.DATA_RECOMMEND);
		recomedSlot.setDataType(CoreConstants.SLOT_RECOMMEND);
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		Slot comAgentSlot = new Slot();
		comAgentSlot.setSlotType(SlotTypes.DATA_AGENT);
		comAgentSlot.setDataType(CoreConstants.SLOT_COMPUTATION_AGENT);
		comAgentSlot.setDescription("Data computed by an agent.");
		
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.DATA_SEARCH);
		searchSlot.setDataType(CoreConstants.SLOT_SEARCH);
		searchSlot.setDescription("Parameters produced by search.");
		
		Slot comAgentErrorSlot = new Slot();
		comAgentErrorSlot.setSlotType(SlotTypes.ERROR);
		comAgentErrorSlot.setDataType(CoreConstants.SLOT_ERRORS);
		comAgentErrorSlot.setDescription("Errors from computing agent.");
		
		return Arrays.asList(
				recomedSlot,
				comAgentSlot,
				searchSlot,
				comAgentErrorSlot);
	}
	
	public static List<Slot> getOutputSlots_CARecSearchComplex()
	{
		return getOutputSlots_CA();
	}
	
	public static List<Slot> getInputSlots_CA()
	{
		Slot inputTrainingSlot = new Slot();
		inputTrainingSlot.setSlotType(SlotTypes.DATA);
		inputTrainingSlot.setDataType(CoreConstants.SLOT_TRAINING_DATA);

		Slot inputTestingSlot = new Slot();
		inputTestingSlot.setSlotType(SlotTypes.DATA);
		inputTestingSlot.setDataType(CoreConstants.SLOT_TESTING_DATA);

		Slot inputValidationSlot = new Slot();
		inputValidationSlot.setSlotType(SlotTypes.DATA);
		inputValidationSlot.setDataType(CoreConstants.SLOT_VALIDATION_DATA);

		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.DATA_EVALUATIONMETHOD);
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
		Slot comAgentDataSlot = new Slot();
		comAgentDataSlot.setSlotType(SlotTypes.DATA_AGENT);
		comAgentDataSlot.setDataType(CoreConstants.SLOT_COMPUTED_DATA);
		comAgentDataSlot.setDescription("Data computed by an agent.");
		
		Slot comAgentErrorSlot = new Slot();
		comAgentErrorSlot.setSlotType(SlotTypes.ERROR);
		comAgentErrorSlot.setDataType(CoreConstants.SLOT_ERRORS);
		comAgentErrorSlot.setDescription("Errors produced by computing agent.");
		
		return Arrays.asList(comAgentDataSlot, comAgentErrorSlot);
	}

	public static List<Slot> getOutputSlots_Search()
	{
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.DATA_SEARCH);
		searchSlot.setDataType(CoreConstants.SLOT_SEARCH);
		searchSlot.setDescription("Parameters produced by search.");
		
		return Arrays.asList(searchSlot);
	}
	
	public static List<Slot> getInputSlots_Recommend()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setSlotType(SlotTypes.ERROR);
		recomedSlot.setDataType(CoreConstants.SLOT_ERRORS);
		recomedSlot.setDescription("Agent errors.");
		
		return Arrays.asList(recomedSlot);
	}

	public static List<Slot> getOutputSlots_Recommend()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setSlotType(SlotTypes.DATA_RECOMMEND);
		recomedSlot.setDataType(CoreConstants.SLOT_RECOMMEND);
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		return Arrays.asList(recomedSlot);
	}
	
	public static List<Slot> getOutputSlots_EvaluationMethod()
	{
		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.DATA_EVALUATIONMETHOD);
		evaluationMethodSlot.setDataType(CoreConstants.SLOT_EVALUATION_METHOD);
		return Arrays.asList(evaluationMethodSlot);
	}
	
	public static List<Slot> getOutputSlot_FileInput()
	{
		Slot fileInputSlot = new Slot();
		fileInputSlot.setSlotType(SlotTypes.DATA);
		fileInputSlot.setDataType(CoreConstants.SLOT_FILE_DATA);
		return  Arrays.asList(fileInputSlot);
	}	

	public static List<Slot> getInputSlot_FileSaver()
	{
		Slot fileSaverSlot = new Slot();
		fileSaverSlot.setSlotType(SlotTypes.DATA);
		fileSaverSlot.setDataType(CoreConstants.SLOT_FILE_DATA);
		return  Arrays.asList(fileSaverSlot);
	}

}