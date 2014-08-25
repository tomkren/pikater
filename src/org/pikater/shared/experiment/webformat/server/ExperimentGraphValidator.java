package org.pikater.shared.experiment.webformat.server;

import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.core.CoreConstants;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.shared.experiment.webformat.server.BoxSlot.SlotType;

public class ExperimentGraphValidator
{
	private final ExperimentGraphServer experimentGraph;
	private final Set<String> problems;
	
	public ExperimentGraphValidator(ExperimentGraphServer experimentGraph)
	{
		this.experimentGraph = experimentGraph;
		this.problems = new LinkedHashSet<String>();
	}
	
	//-----------------------------------------------------------
	// BASIC PUBLIC INTERFACE
	
	public boolean problemsFound()
	{
		return !this.problems.isEmpty();
	}
	
	public Set<String> getProblems()
	{
		return problems;
	}
	
	public void clear()
	{
		this.problems.clear();
	}
	
	//-----------------------------------------------------------
	// BASIC PRIVATE INTERFACE
	
	private void registerValidationProblem(String problem)
	{
		this.problems.add(problem);
	}
	
	// ------------------------------------------------------------------
	// VALIDATION INTERFACE
	
	public void validate()
	{
		// clean the instance just in case so that old problems are not effective anymore
		clear();
		
		// first check boxes
		for(BoxInfoServer box : experimentGraph)
		{
			if(box.isRegistered()) // only inspect registered boxes
			{
				/*
				 * A check whether the box is approved is needed. Currently, this is done
				 * in core system and only approved agents are sent to web.
				 */
				if(!experimentGraph.getSlotConnections().hasBoxSlotConnections(box))
				{
					// the box is not isolated but has no valid slot connections defined
					registerValidationProblem(String.format("At least one of '%s' boxes has no slot connections defined. Isolated boxes are not allowed.", 
							box.getAssociatedAgent().getName()));
				}
				else
				{
					switch(box.getBoxType())
					{
						case INPUT:
							validateBoxSlot(box, SlotType.OUTPUT, CoreConstants.SLOT_FILE_DATA); // potentially duplicate check but it is needed
							break;
						case CHOOSE:
							break;
						case COMPOSITE:
							break;
						case COMPUTE:
							validateBoxSlot(box, SlotType.INPUT, CoreConstants.SLOT_TRAINING_DATA); // potentially duplicate check but it is needed
							validateBoxSlot(box, SlotType.INPUT, CoreConstants.SLOT_TESTING_DATA); // potentially duplicate check but it is needed
							break;
						case MISC:
							break;
						case OPTION:
							break;
						case OUTPUT:
							validateBoxSlot(box, SlotType.INPUT, CoreConstants.SLOT_FILE_DATA); // potentially duplicate check but it is needed
							break;
						case PROCESS_DATA:
							break;
						case SEARCH:
							break;
						default:
							throw new IllegalStateException("Unknown state: " + box.getBoxType().name());
					}
				}
			}
		}
		
		// then check edges
		/*
		for(BoxInfoServer boxFrom : experimentGraph)
		{
			for(BoxInfoServer boxTo : experimentGraph.getFromNeighbours(boxFrom.getID()))
			{
				if(boxFrom.isRegistered() != boxTo.isRegistered())
				{
					registerValidationProblem(String.format("At least one of '%s' boxes doesn't have the '%s' slot connected."));
				}
			}
		}
		*/
	}
	
	private void validateBoxSlot(BoxInfoServer box, SlotType type, String slotDataType)
	{
		Slot slotToCheck;
		if(type == SlotType.INPUT)
		{
			slotToCheck = box.getAssociatedAgent().fetchInputSlotByDataType(slotDataType);
		}
		else
		{
			slotToCheck = box.getAssociatedAgent().fetchOutputSlotByDataType(slotDataType);	
		}
		if(!experimentGraph.getSlotConnections().isSlotConnectedToAValidEndpoint(slotToCheck))
		{
			registerValidationProblem(String.format("At least one of '%s' boxes doesn't have the '%s' slot connected.", 
					box.getAssociatedAgent().getName(), slotDataType));
		}
	}
}