package pikater.utility.boxTypes;

import pikater.ontology.description.FileDataProvider;
import pikater.utility.boxTypes.BoxesModel.BoxCategory;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UStringParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataProviderSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class InputBox extends Box
{
	public InputBox()
	{
		super("FileInput", "pikater/Agent_FileInput", BoxCategory.INPUT.name(), FileDataProvider.class, "picture0.jpg", "Box is a file data source.");
		
		UStringParameter boxParam_File = new UStringParameter();
		boxParam_File.setName("FileName");
		boxParam_File.setValue("file.txt");
		addParameter(boxParam_File);
		
		UniversalSlot boxDataOutputSlots = new DataProviderSlot();
		boxDataOutputSlots.setSlotName("Dataset");
		addOutputSlot(boxDataOutputSlots);
	}
}