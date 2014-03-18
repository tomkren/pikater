package pikater.utility.pikaterDatabase.tables.experiments;

import java.util.ArrayList;

import pikater.utility.boxTypes.Box;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UniversalParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.ParameterConsumerSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class UniversalDataProvider extends UniversalDataFlow {


	private ArrayList<UniversalSlot> inputSlots = new ArrayList<UniversalSlot>();
	private ArrayList<UniversalSlot> outputSlots = new ArrayList<UniversalSlot>();

	
	public UniversalSlot getInputSlotByName(String name) {
		
		for (UniversalSlot slotI : this.inputSlots) {
			if (slotI.getSlotName().equals(name)) {
				return slotI;
			}
		}
		return null;
	}
	public UniversalSlot getOutputSlotByName(String name) {
		
		for (UniversalSlot slotI : this.outputSlots) {
			if (slotI.getSlotName().equals(name)) {
				return slotI;
			}
		}
		return null;
	}
	
	public void setBoxDescription(Box boxDescription) {
		super.boxDescription = boxDescription;

		//TODO: better to create copy of slots and parameters
		for ( UniversalParameter parameterI : boxDescription.getParameters() ) {
			
			ParameterConsumerSlot paramSlot = new ParameterConsumerSlot();
			paramSlot.setParameter(parameterI);
			inputSlots.add(paramSlot);
		}

		for ( UniversalSlot slotI : boxDescription.getInputSlots() ) {
			inputSlots.add(slotI);
		}

		for ( UniversalSlot slotI : boxDescription.getOutputSlots() ) {
			outputSlots.add(slotI);
		}
		
		
	}


}