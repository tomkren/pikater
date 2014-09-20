package org.pikater.core.options;

import java.util.Arrays;
import java.util.List;

import org.pikater.core.CoreConstant.SlotContent;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;

public abstract class SlotsHelper
{
	//-------------------------------------------------------
	// INDIVIDUAL SLOT DEFINITIONS
	
	public static List<Slot> getIntputSlots_CARecSearchComplex()
	{
		Slot comAgentSlot = new Slot(SlotContent.COMPUTATION_AGENT, "Data computed by an agent.");
		Slot comAgentErrorSlot = new Slot(SlotContent.ERRORS, "Errors from computing agent.");
		Slot recommendSlot = new Slot(SlotContent.RECOMMEND, "Recommends an agent to use (its name)."); 
		Slot searchSlot = new Slot(SlotContent.SEARCH, "Parameters produced by search.");
		 
		return Arrays.asList(
				recommendSlot,
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
		Slot trainingSlot = new Slot(SlotContent.TRAINING_DATA);
		Slot testingSlot = new Slot(SlotContent.TESTING_DATA);
		Slot validationSlot = new Slot(SlotContent.VALIDATION_DATA);
		Slot evaluationMethodSlot = new Slot(SlotContent.EVALUATION_METHOD);
		
		return Arrays.asList(
				trainingSlot,
				testingSlot,
				validationSlot,
				evaluationMethodSlot
		);
	}

	public static List<Slot> getOutputSlots_CA()
	{
		Slot comAgentDataSlot = new Slot(SlotContent.COMPUTED_DATA, "Data computed by an agent."); 
		Slot comAgentErrorSlot = new Slot(SlotContent.ERRORS, "Errors produced by computing agent."); 
		return Arrays.asList(comAgentDataSlot, comAgentErrorSlot);
	}

	public static List<Slot> getOutputSlots_Search()
	{
		Slot searchSlot = new Slot(SlotContent.SEARCH, "Parameters produced by search.");
		Slot searchErrorSlot = new Slot(SlotContent.ERRORS, "Errors produced by search."); 
		return Arrays.asList(searchSlot, searchErrorSlot);
	}
	
	public static List<Slot> getInputSlots_Recommend()
	{
		Slot recommendSlot = new Slot(SlotContent.ERRORS, "Agent errors.");
		return Arrays.asList(recommendSlot);
	}

	public static List<Slot> getOutputSlots_Recommend()
	{
		Slot recomedSlot = new Slot(SlotContent.RECOMMEND, "Recommends an agent to use (its name)."); 
		return Arrays.asList(recomedSlot);
	}
	
	public static List<Slot> getOutputSlots_EvaluationMethod()
	{
		Slot evaluationMethodSlot = new Slot(SlotContent.EVALUATION_METHOD);
		return Arrays.asList(evaluationMethodSlot);
	}
	
	public static List<Slot> getOutputSlot_FileInput()
	{
		Slot fileInputSlot = new Slot(SlotContent.FILE_DATA);
		return  Arrays.asList(fileInputSlot);
	}	

	public static List<Slot> getInputSlot_FileSaver()
	{
		Slot fileSaverSlot = new Slot(SlotContent.FILE_DATA);
		return  Arrays.asList(fileSaverSlot);
	}

}