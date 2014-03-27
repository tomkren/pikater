package pikater.utility.boxTypes;

import pikater.ontology.description.ComputingAgent;
import pikater.utility.boxTypes.BoxesModel.BoxCategory;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerInterval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataConsumerSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataProviderSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class CrossValidationBox extends Box
{
	public CrossValidationBox()
	{
		super("CrossValidation-Method", "pikater/Agent_CrossValidation", BoxCategory.COMPUTING.name(), ComputingAgent.class, "picture3.jpg", "Computing agent which used for training neural "
				+ "networks deterministic library WEKA. As a training method is used default Cross Validation WEKA method.");
		
		UIntegerInterval interval = new UIntegerInterval();
		interval.setMin(1);
		interval.setMax(100);
		UIntegerParameter boxParam_F = new UIntegerParameter();
		boxParam_F.setName("F");
		boxParam_F.setValue(5);
		boxParam_F.setRange(interval);
		addParameter(boxParam_F);

		UniversalSlot boxTrainingDataInputSlots = new DataConsumerSlot();
		boxTrainingDataInputSlots.setSlotName("Training data");
		addInputSlot(boxTrainingDataInputSlots);

		UniversalSlot boxTestingDataInputSlots = new DataConsumerSlot();
		boxTestingDataInputSlots.setSlotName("Testing data");
		addInputSlot(boxTestingDataInputSlots);
		
		UniversalSlot boxValidationDataInputSlots = new DataConsumerSlot();
		boxValidationDataInputSlots.setSlotName("Validation data");
		addInputSlot(boxValidationDataInputSlots);

		UniversalSlot boxdataOutputSlots = new DataProviderSlot();
		boxdataOutputSlots.setSlotName("Computed data");
		addOutputSlot(boxdataOutputSlots);
	}
}
