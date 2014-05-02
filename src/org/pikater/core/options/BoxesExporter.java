package org.pikater.core.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.pikater.core.ontology.description.CARecSearchComplex;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.Recommend;
import org.pikater.core.ontology.description.Search;
import org.pikater.shared.experiment.box.BoxInfo;
import org.pikater.shared.experiment.box.BoxInfo.BoxType;

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

	public ArrayList<BoxInfo> getBoxexOfType(BoxType type) {
		
		ArrayList<Class> searchOntology = new ArrayList<Class>();
		
		
		if (type == BoxType.INPUT) {
			
			searchOntology.add(FileDataProvider.class);			
	
		} else if (type == BoxType.COMPUTING) {
			
			searchOntology.add(ComputingAgent.class);
			
		} else if (type == BoxType.SEARCHER) {
			
			searchOntology.add(Search.class);
		
		} else if (type == BoxType.RECOMMENDER) {
			
			searchOntology.add(Recommend.class);
		
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

		ArrayList<BoxInfo> transformedBoxes =
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

	private ArrayList<BoxInfo> transformations(
			ArrayList<LogicalBoxDescription> descriptions, BoxType type) {
		
		ArrayList<BoxInfo> boxes = new ArrayList<BoxInfo>();
		
		for (LogicalBoxDescription logicDescrI : descriptions) {
			boxes.add(transformation(logicDescrI, type));
		}
		
		return boxes;
	}

	private BoxInfo transformation(LogicalBoxDescription description, BoxType type) {
		
		BoxInfo box = new BoxInfo();
		box.setType(type);

		
		return box;
	}
	
	private ArrayList<BoxInfo> getWrapperBoxes() {
		
		LogicalBoxDescription treeLogicalBox =
				new LogicalBoxDescription(
						"Complex",
						CARecSearchComplex.class,
						"Wraper for tree boxes, Computing, Search and Recomend"
						);

		treeLogicalBox.setPicture("complex.jpg");

		BoxInfo treeBox = transformation(treeLogicalBox, BoxType.WRAPPER);

		ArrayList<BoxInfo> wrappers = new ArrayList<BoxInfo>();
		wrappers.add(treeBox);

		return wrappers;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		BoxesExporter be = new BoxesExporter();
		be.importXMLs();
	}

}
