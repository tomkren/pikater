package pikater.utility.pikaterDatabase.tables.experiments;

import java.io.IOException;

import pikater.utility.boxTypes.BoxesModel;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataConsumerSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataProviderSlot;


public class Experiment {

	public static void experiment1() throws IOException {

		UniversalDataProvider inputFile = new UniversalDataProvider();
		inputFile.setBoxDescription(BoxesModel.boxes.get(BoxesModel.box_fileInput));
		DataProviderSlot inputFileDatasetOutputSlot =
				(DataProviderSlot) inputFile.getOutputSlotByName("Dataset");

		
		UniversalDataProvider computing = new UniversalDataProvider();
		computing.setBoxDescription(BoxesModel.boxes.get(BoxesModel.box_visualizer));
		DataConsumerSlot computingTrainingDataInputSlot =
				(DataConsumerSlot) computing.getInputSlotByName("Training data");
		DataConsumerSlot computingTestingDataInputSlot =
				(DataConsumerSlot) computing.getInputSlotByName("Testing data");
		DataConsumerSlot computingValidationDataInputSlot =
				(DataConsumerSlot) computing.getInputSlotByName("Validation data");
		DataProviderSlot computingDataOutputSlot =
				(DataProviderSlot) computing.getOutputSlotByName("Output Data");

		computingTrainingDataInputSlot.connect(inputFileDatasetOutputSlot);
		computingTestingDataInputSlot.connect(inputFileDatasetOutputSlot);
		computingValidationDataInputSlot.connect(inputFileDatasetOutputSlot);


		UniversalDataConsumer visualizer = new UniversalDataConsumer();
		visualizer.setBoxDescription(BoxesModel.boxes.get(BoxesModel.box_visualizer));
		DataConsumerSlot visualizerInputSlot =
				(DataConsumerSlot) visualizer.getInputSlotByName("Data Source");
		visualizerInputSlot.connect(computingDataOutputSlot);


		UniversalComputingDescription comDesc = new UniversalComputingDescription();
		comDesc.addDataFlow(inputFile);
		comDesc.addDataFlow(computing);
		comDesc.addDataFlow(visualizer);
		comDesc.setHadGraphicalInput(true);

	}
}
