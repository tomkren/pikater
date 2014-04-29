package org.pikater.core.ontology.description;

import jade.content.Concept;
import jade.util.leap.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.pikater.shared.database.experiment.UniversalComputationDescription;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputationDescription implements Concept {

	private static final long serialVersionUID = -7951850172320173523L;

	private ArrayList globalOptions;
    private ArrayList rootElements;

    public ArrayList getGlobalOptions() {
        return globalOptions;
    }
    public void setGlobalOptions(ArrayList globalOptions) {
        this.globalOptions = globalOptions;
    }

    public ArrayList getRootElements() {
        return rootElements;
    }
    public void setRootElements(ArrayList rootElements) {
        this.rootElements = rootElements;
    }
    public void addRootElement(FileDataSaver rootElement) {
    	
    	if (rootElements == null) {
    		this.rootElements = new ArrayList();
    	}
    	this.rootElements.add(rootElement);
    }
    
	public UniversalComputationDescription ExportUniversalComputationDescription() {
		
		ArrayList options = getGlobalOptions();
		ArrayList rootElements = getRootElements();

		UniversalComputationDescription uModel =
				new UniversalComputationDescription();
		uModel.setGlobalOptions(options);

		for (int i = 0; i < rootElements.size(); i++) {
			
			FileDataSaver saver = (FileDataSaver) rootElements.get(i);
			saver.exportUniversalElement(uModel);
		}

		return uModel;
	}
 
	public String exportXML(String fileName) throws FileNotFoundException {

		XStream xstream = new XStream();

		//Class<ComputationDescription> descriptionOntology =
		//		org.pikater.core.ontology.description.ComputationDescription.class;
		
		//xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");

		String xml = xstream.toXML(this);

		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();

		return xml;
	}

	public static ComputationDescription importXML(String fileName) throws FileNotFoundException {

		XStream xstream = new XStream();

		//Class<ComputationDescription> descriptionOntology =
		//		org.pikater.core.ontology.description.ComputationDescription.class;

		//xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");

		Scanner scanner = new Scanner(new File(fileName));
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		ComputationDescription computDes =
				(ComputationDescription)xstream.fromXML(xml);
		
		return computDes;
	}
	
}
