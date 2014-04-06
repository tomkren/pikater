package org.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BoxesExporter {

	public List<LogicalUnit> export() throws FileNotFoundException {
		
		String filePath = 
				System.getProperty("user.dir") +
				System.getProperty("file.separator") + "src" +
				System.getProperty("file.separator") + "org" +
				System.getProperty("file.separator") + "options" +
				System.getProperty("file.separator");

		List<LogicalUnit> listOfBoxes = new ArrayList<LogicalUnit>();
		
		File crossValidationFile =
				new File(filePath + "CrossValidationBox.xml");
		LogicalUnit crossValidationBox =
				LogicalUnit.importXML(crossValidationFile);
		listOfBoxes.add(crossValidationBox);
		
		File differenceVisualizerFile =
				new File(filePath + "DifferenceVisualizerBox.xml");
		LogicalUnit differenceVisualizerBox =
				LogicalUnit.importXML(differenceVisualizerFile);
		listOfBoxes.add(differenceVisualizerBox);
		
		File fileInputFile =
				new File(filePath + "FileInputBox.xml");
		LogicalUnit fileVisualizerBox =
				LogicalUnit.importXML(fileInputFile);
		listOfBoxes.add(fileVisualizerBox);
		
		File fileVisualizerFile =
				new File(filePath + "FileVisualizerBox.xml");
		LogicalUnit fileInputBox =
				LogicalUnit.importXML(fileVisualizerFile);
		listOfBoxes.add(fileInputBox);

		File randomSearchFile =
				new File(filePath + "RandomSearchBox.xml");
		LogicalUnit randomSearchBox =
				LogicalUnit.importXML(randomSearchFile);
		listOfBoxes.add(randomSearchBox);

		File SimulatedAnnealingFile =
				new File(filePath + "SimulatedAnnealingBox.xml");
		LogicalUnit randomSearchBoxBox =
				LogicalUnit.importXML(SimulatedAnnealingFile);
		listOfBoxes.add(randomSearchBoxBox);
		
		return listOfBoxes;
	}

	public static void main(String [ ] args) throws FileNotFoundException {
		
		BoxesExporter export = new BoxesExporter();
		List<LogicalUnit> list = export.export();
	}
}
