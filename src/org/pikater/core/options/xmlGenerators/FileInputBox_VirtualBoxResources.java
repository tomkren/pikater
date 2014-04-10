package org.pikater.core.options.xmlGenerators;

import org.pikater.shared.experiment.resources.DataResource;
import org.pikater.shared.experiment.resources.ParamResource;

public class FileInputBox_VirtualBoxResources
{
	public static final ParamResource param_inputFileName = new ParamResource("File name", null);
	public static final DataResource data_parsedInput = new DataResource(param_inputFileName,
			"Data parsed from the file provided by the 'param_inputFileName' parameter.");
}
