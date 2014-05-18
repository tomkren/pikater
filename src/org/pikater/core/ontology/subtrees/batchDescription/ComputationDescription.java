package org.pikater.core.ontology.subtrees.batchDescription;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputationDescription implements Concept {

	private static final long serialVersionUID = -7951850172320173523L;

	private List<Option> globalOptions;
    private List<FileDataSaver> rootElements;

    public List<Option> getGlobalOptions() {
        return globalOptions;
    }
    public void setGlobalOptions(ArrayList<Option> globalOptions) {
        this.globalOptions = globalOptions;
    }

    public List<FileDataSaver> getRootElements() {
    	if (rootElements == null) {
    		return new ArrayList<FileDataSaver>();
    	}
        return rootElements;
    }
    public void setRootElements(List<FileDataSaver> rootElements) {
        this.rootElements = rootElements;
    }
    public void addRootElement(FileDataSaver rootElement) {
    	if (rootElements == null) {
    		this.rootElements = new ArrayList<FileDataSaver>();
    	}
    	this.rootElements.add(rootElement);
    }
    
	public UniversalComputationDescription ExportUniversalComputationDescription() {

		List<FileDataSaver> rootElements = getRootElements();

		UniversalComputationDescription uModel =
				new UniversalComputationDescription();

		if (getGlobalOptions() != null) {
			for (int i = 0; i < getGlobalOptions().size(); i++) {
				Option optionI = (Option) getGlobalOptions().get(i);
				uModel.addGlobalOption( optionI );
			}
		}

		if (rootElements != null) {
			for (int i = 0; i < rootElements.size(); i++) {
				
				FileDataSaver saver = rootElements.get(i);
				saver.exportUniversalElement(uModel);
			}
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
