package pikater.utility.experiment;

import java.io.IOException;

public class Example
{
	public static void experiment1() throws IOException
	{
		/*
		UniversalDataProvider inputFile = new UniversalDataProvider();
		inputFile.setBoxDescription(DefinedBoxes.boxes
				.get(DefinedBoxes.box_fileInput));
		DataProviderSlot inputFileDatasetOutputSlot = (DataProviderSlot) inputFile
				.getOutputSlotByName("Dataset");

		UniversalDataProvider computing = new UniversalDataProvider();
		computing.setBoxDescription(DefinedBoxes.boxes
				.get(DefinedBoxes.box_visualizer));
		DataConsumerSlot computingTrainingDataInputSlot = (DataConsumerSlot) computing
				.getInputSlotByName("Training data");
		DataConsumerSlot computingTestingDataInputSlot = (DataConsumerSlot) computing
				.getInputSlotByName("Testing data");
		DataConsumerSlot computingValidationDataInputSlot = (DataConsumerSlot) computing
				.getInputSlotByName("Validation data");
		DataProviderSlot computingDataOutputSlot = (DataProviderSlot) computing
				.getOutputSlotByName("Output Data");

		computingTrainingDataInputSlot.connect(inputFileDatasetOutputSlot);
		computingTestingDataInputSlot.connect(inputFileDatasetOutputSlot);
		computingValidationDataInputSlot.connect(inputFileDatasetOutputSlot);

		UniversalDataConsumer visualizer = new UniversalDataConsumer();
		visualizer.setBoxDescription(DefinedBoxes.boxes
				.get(DefinedBoxes.box_visualizer));
		DataConsumerSlot visualizerInputSlot = (DataConsumerSlot) visualizer
				.getInputSlotByName("Data Source");
		visualizerInputSlot.connect(computingDataOutputSlot);

		SchemaDataSource comDesc = new SchemaDataSource();
		comDesc.addDataFlow(inputFile);
		comDesc.addDataFlow(computing);
		comDesc.addDataFlow(visualizer);
		comDesc.setHadGraphicalInput(true);
		*/
	}
}
