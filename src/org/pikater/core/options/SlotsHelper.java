package org.pikater.core.options;

import java.util.Arrays;
import java.util.List;

import org.pikater.core.CoreConstant;
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
		recomedSlot.setDataType(CoreConstant.Slot.SLOT_RECOMMEND.get());
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		Slot comAgentSlot = new Slot();
		comAgentSlot.setSlotType(SlotTypes.DATA_AGENT);
		comAgentSlot.setDataType(CoreConstant.Slot.SLOT_COMPUTATION_AGENT.get());
		comAgentSlot.setDescription("Data computed by an agent.");
		
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.DATA_SEARCH);
		searchSlot.setDataType(CoreConstant.Slot.SLOT_SEARCH.get());
		searchSlot.setDescription("Parameters produced by search.");
		
		Slot comAgentErrorSlot = new Slot();
		comAgentErrorSlot.setSlotType(SlotTypes.ERROR);
		comAgentErrorSlot.setDataType(CoreConstant.Slot.SLOT_ERRORS.get());
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
		inputTrainingSlot.setDataType(CoreConstant.Slot.SLOT_TRAINING_DATA.get());

		Slot inputTestingSlot = new Slot();
		inputTestingSlot.setSlotType(SlotTypes.DATA);
		inputTestingSlot.setDataType(CoreConstant.Slot.SLOT_TESTING_DATA.get());

		Slot inputValidationSlot = new Slot();
		inputValidationSlot.setSlotType(SlotTypes.DATA);
		inputValidationSlot.setDataType(CoreConstant.Slot.SLOT_VALIDATION_DATA.get());

		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.DATA_EVALUATIONMETHOD);
		evaluationMethodSlot.setDataType(CoreConstant.Slot.SLOT_EVALUATION_METHOD.get());
		
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
		comAgentDataSlot.setDataType(CoreConstant.Slot.SLOT_COMPUTED_DATA.get());
		comAgentDataSlot.setDescription("Data computed by an agent.");
		
		Slot comAgentErrorSlot = new Slot();
		comAgentErrorSlot.setSlotType(SlotTypes.ERROR);
		comAgentErrorSlot.setDataType(CoreConstant.Slot.SLOT_ERRORS.get());
		comAgentErrorSlot.setDescription("Errors produced by computing agent.");
		
		return Arrays.asList(comAgentDataSlot, comAgentErrorSlot);
	}

	public static List<Slot> getOutputSlots_Search()
	{
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.DATA_SEARCH);
		searchSlot.setDataType(CoreConstant.Slot.SLOT_SEARCH.get());
		searchSlot.setDescription("Parameters produced by search.");
		
		Slot searchErrorSlot = new Slot();
		searchErrorSlot.setSlotType(SlotTypes.ERROR);
		searchErrorSlot.setDataType(CoreConstant.Slot.SLOT_ERRORS.get());
		searchErrorSlot.setDescription("Errors produced by search.");
		
		return Arrays.asList(searchSlot, searchErrorSlot);
	}
	
	public static List<Slot> getInputSlots_Recommend()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setSlotType(SlotTypes.ERROR);
		recomedSlot.setDataType(CoreConstant.Slot.SLOT_ERRORS.get());
		recomedSlot.setDescription("Agent errors.");
		
		return Arrays.asList(recomedSlot);
	}

	public static List<Slot> getOutputSlots_Recommend()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setSlotType(SlotTypes.DATA_RECOMMEND);
		recomedSlot.setDataType(CoreConstant.Slot.SLOT_RECOMMEND.get());
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		return Arrays.asList(recomedSlot);
	}
	
	public static List<Slot> getOutputSlots_EvaluationMethod()
	{
		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.DATA_EVALUATIONMETHOD);
		evaluationMethodSlot.setDataType(CoreConstant.Slot.SLOT_EVALUATION_METHOD.get());
		return Arrays.asList(evaluationMethodSlot);
	}
	
	public static List<Slot> getOutputSlot_FileInput()
	{
		Slot fileInputSlot = new Slot();
		fileInputSlot.setSlotType(SlotTypes.DATA);
		fileInputSlot.setDataType(CoreConstant.Slot.SLOT_FILE_DATA.get());
		return  Arrays.asList(fileInputSlot);
	}	

	public static List<Slot> getInputSlot_FileSaver()
	{
		Slot fileSaverSlot = new Slot();
		fileSaverSlot.setSlotType(SlotTypes.DATA);
		fileSaverSlot.setDataType(CoreConstant.Slot.SLOT_FILE_DATA.get());
		return  Arrays.asList(fileSaverSlot);
	}

}