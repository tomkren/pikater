package org.pikater.core.options;

import java.util.Arrays;
import java.util.List;

import org.pikater.core.CoreConstant.SlotContent;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;

/**
 * 
 * Abstract class which helps with the definition of slots in the new Box.
 * Each Box contains a ontology, by this type of ontology you can choose
 * the right slot definition
 * 
 */
public abstract class SlotsHelper {
	//-------------------------------------------------------
	// INDIVIDUAL SLOT DEFINITIONS
	
	/**
	 * Get the input slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getIntputSlots_CARecSearchComplex() {
		
		Slot comAgentSlot = new Slot(SlotContent.COMPUTATION_AGENT,
				"Data computed by an agent.");
		Slot comAgentErrorSlot = new Slot(SlotContent.ERRORS,
				"Errors from computing agent.");
		Slot recommendSlot = new Slot(SlotContent.RECOMMEND,
				"Recommends an agent to use (its name)."); 
		Slot searchSlot = new Slot(SlotContent.SEARCH,
				"Parameters produced by search.");
		 
		return Arrays.asList(
				recommendSlot,
				comAgentSlot,
				searchSlot,
				comAgentErrorSlot);
	}

	/**
	 * Get the output slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getOutputSlots_CARecSearchComplex() {
		return getOutputSlots_CA();
	}
	
	/**
	 * Get the input slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getInputSlots_CA() {
		Slot trainingSlot = new Slot(SlotContent.TRAINING_DATA);
		Slot testingSlot = new Slot(SlotContent.TESTING_DATA);
		Slot validationSlot = new Slot(SlotContent.VALIDATION_DATA);
		Slot dataToLabelSlot = new Slot(SlotContent.DATA_TO_LABEL);
		Slot evaluationMethodSlot = new Slot(SlotContent.EVALUATION_METHOD);
		
		return Arrays.asList(
				trainingSlot,
				testingSlot,
				validationSlot,
				dataToLabelSlot,
				evaluationMethodSlot
		);
	}

	/**
	 * Get the output slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getOutputSlots_CA() {
		Slot comAgentDataSlot = new Slot(SlotContent.COMPUTED_DATA,
				"Data computed by an agent."); 
		Slot comAgentErrorSlot = new Slot(SlotContent.ERRORS,
				"Errors produced by computing agent."); 
		return Arrays.asList(comAgentDataSlot, comAgentErrorSlot);
	}

	/**
	 * Get the output slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getOutputSlots_Search() {
		Slot searchSlot = new Slot(SlotContent.SEARCH,
				"Parameters produced by search.");
		Slot searchErrorSlot = new Slot(SlotContent.ERRORS,
				"Errors produced by search."); 
		return Arrays.asList(searchSlot, searchErrorSlot);
	}

	/**
	 * Get the input slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getInputSlots_Recommend() {
		Slot recommendSlot = new Slot(SlotContent.ERRORS,
				"Agent errors.");
		return Arrays.asList(recommendSlot);
	}

	/**
	 * Get the output slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getOutputSlots_Recommend() {
		Slot recomedSlot = new Slot(SlotContent.RECOMMEND,
				"Recommends an agent to use (its name)."); 
		return Arrays.asList(recomedSlot);
	}
	
	/**
	 * Get the output slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getOutputSlots_EvaluationMethod() {
		Slot evaluationMethodSlot = new Slot(SlotContent.EVALUATION_METHOD);
		return Arrays.asList(evaluationMethodSlot);
	}
	
	/**
	 * Get the output slots definition for the ontology
	 * @return - list of slots
	 */
	public static List<Slot> getOutputSlot_FileInput() {
		Slot fileInputSlot = new Slot(SlotContent.FILE_DATA);
		return  Arrays.asList(fileInputSlot);
	}	

	/**
	 * Get the input slots definition for the ontology
	 * {@link FileSaver}
	 * @return - list of slots
	 */
	public static List<Slot> getInputSlot_FileSaver() {
		Slot fileSaverSlot = new Slot(SlotContent.FILE_DATA);
		return  Arrays.asList(fileSaverSlot);
	}

}