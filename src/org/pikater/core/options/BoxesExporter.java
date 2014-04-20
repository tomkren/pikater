package org.pikater.core.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.pikater.core.ontology.description.CARecSearchComplex;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.Recommen;
import org.pikater.core.ontology.description.Search;
import org.pikater.shared.experiment.Box;
import org.pikater.shared.experiment.BoxType;

// TODO: this class is pointless... merge with BoxLoader or OptionLoader in the "shared" package.
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
	
	public ArrayList<Box> getBoxexOfType(BoxType type) {
		
		ArrayList<Class> searchOntology = new ArrayList<Class>();
		
		
		if (type == BoxType.INPUT) {
			
			searchOntology.add(FileDataProvider.class);			
	
		} else if (type == BoxType.COMPUTING) {
			
			searchOntology.add(ComputingAgent.class);
			
		} else if (type == BoxType.SEARCH) {
			
			searchOntology.add(Search.class);
		
		} else if (type == BoxType.RECOMMEND) {
			
			searchOntology.add(Recommen.class);
		
		} else if (type == BoxType.METHOD) {
			
			searchOntology.add(Method.class);
			
		} else if (type == BoxType.VISUALIZER) {
			
			searchOntology.add(FileDataProvider.class);
			
		} else if (type == BoxType.WRAPPER) {
			
			return getWrapperBoxes();
			
		} else {
			return null;
		}

		ArrayList<LogicalBoxDescription> selectedBoxes =
				getLogicalBoxesWith(logicalBoxes, searchOntology);

		ArrayList<Box> transformedBoxes =
				transformations(selectedBoxes, type);

		return transformedBoxes;
	}
	
	private ArrayList<LogicalBoxDescription> getLogicalBoxesWith(
			ArrayList<LogicalBoxDescription> boxes, ArrayList<Class> ontology) {
		
		ArrayList<LogicalBoxDescription> logicalBoxes =
				new ArrayList<LogicalBoxDescription>();
		
		for (LogicalBoxDescription logBoxI: boxes) {
			
			for (Class ontologyI: ontology) {

				if (logBoxI.getOntology() == ontologyI) {
					logicalBoxes.add(logBoxI);
				}
			}
		}
		
		return logicalBoxes;
	}

	private ArrayList<Box> transformations(
			ArrayList<LogicalBoxDescription> descriptions, BoxType type) {
		
		ArrayList<Box> boxes = new ArrayList<Box>();
		
		for (LogicalBoxDescription logicDescrI : descriptions) {
			boxes.add(transformation(logicDescrI, type));
		}
		
		return boxes;
	}

	private Box transformation(LogicalBoxDescription description, BoxType type) {
		
		Box box = new Box();
		box.setType(type);

		
		return box;
	}
	
	private ArrayList<Box> getWrapperBoxes() {
		
		LogicalBoxDescription treeLogicalBox =
				new LogicalBoxDescription(
						"Complex",
						CARecSearchComplex.class,
						"Wraper for tree boxes, Computing, Search and Recomend"
						);

		treeLogicalBox.setPicture("complex.jpg");

		Box treeBox = transformation(treeLogicalBox, BoxType.WRAPPER);

		ArrayList<Box> wrappers = new ArrayList<Box>();
		wrappers.add(treeBox);

		return wrappers;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		BoxesExporter be = new BoxesExporter();
		be.importXMLs();
	}
}
