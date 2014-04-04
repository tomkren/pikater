package pikater.utility.experiment.boxes.specific;

import java.util.EnumSet;
import java.util.Set;

import pikater.utility.experiment.boxes.core.LeafBox;
import pikater.utility.experiment.parameters.ValueParameter;
import pikater.utility.experiment.parameters.info.InputBoxParams;
import pikater.utility.experiment.slots.DataSlot;

public class InputBox extends LeafBox
{
	private final Set<LeafBoxCategory> categories;
	
	public InputBox()
	{
		super("FileInput", "picture0.jpg", "This box provides a data source to other boxes.", "Agent_FileInput", "FileDataProvider");
		
		this.categories = EnumSet.of(LeafBoxCategory.INPUT);
		
		addEditableParameter(InputBoxParams.fileName, new ValueParameter<String>("file.txt"));
		
		addOutputSlot(new DataSlot("Dataset"));
	}

	@Override
	public Set<LeafBoxCategory> getCategories()
	{
		return categories;
	}
}
