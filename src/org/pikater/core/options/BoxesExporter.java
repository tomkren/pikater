package org.pikater.core.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BoxesExporter {

	private ArrayList<LogicalBoxDescription> logicalBoxes =
			new ArrayList<LogicalBoxDescription>();
	
	public void importXMLs() throws FileNotFoundException {
		
		System.out.println("Importing Boxes from configuration XMLs");
		String filePath = LogicalUnitDescription.filePath;
		
		
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if (listOfFiles[i].isFile()) {
	    		
	    		String fileName = listOfFiles[i].getName();
	    		if (! fileName.endsWith(".xml")) {
	    			continue;
	    		}
	    			    		
	    		File fileI = new File(filePath + fileName);
	    		
	    		LogicalUnitDescription logUnit =
	    				LogicalUnitDescription.importXML(fileI);
	    		
	    		if (logUnit instanceof LogicalBoxDescription)  {
	    			System.out.println("Added Box: " + fileName);
	    			logicalBoxes.add((LogicalBoxDescription) logUnit);
	    		}
	        }
	    }

	}

	public static void main(String[] args) throws FileNotFoundException {
		
		BoxesExporter be = new BoxesExporter();
		be.importXMLs();
	}
}
