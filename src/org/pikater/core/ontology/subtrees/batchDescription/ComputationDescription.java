package org.pikater.core.ontology.subtrees.batchDescription;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalElement;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputationDescription implements Concept {

	private static final long serialVersionUID = -7951850172320173523L;

	private List<Option> globalOptions = new ArrayList<Option>();
	private List<FileDataSaver> rootElements = new ArrayList<FileDataSaver>();

	public List<Option> getGlobalOptions() {
		return globalOptions;
	}

	public void setGlobalOptions(List<Option> globalOptions) {
		if (globalOptions == null) {
			throw new IllegalArgumentException("Argument globalOptions can't be null");
		}
		this.globalOptions = globalOptions;
	}

	public List<FileDataSaver> getRootElements() {
		return rootElements;
	}

	public void setRootElements(List<FileDataSaver> rootElements) {
		if (rootElements == null) {
			throw new IllegalArgumentException("Argument rootElements can't be null");
		}
		this.rootElements = rootElements;
	}

	public void addRootElement(FileDataSaver rootElement) {
		if (rootElement == null) {
			throw new IllegalArgumentException("Argument rootElement can't be null");
		}
		this.rootElements.add(rootElement);
	}

	public UniversalComputationDescription exportUniversalComputationDescription() {

		UniversalComputationDescription uModel = new UniversalComputationDescription();
		uModel.addGlobalOptions(new HashSet<Option>(this.getGlobalOptions()));

		for (FileDataSaver saverI : getRootElements()) {
			saverI.exportUniversalElement(uModel);
		}

		return uModel;
	}

	public static ComputationDescription importUniversalComputationDescription(
			UniversalComputationDescription uDescription) {
		
		ComputationDescription compDescription = new ComputationDescription();
		
		List<Option> globalOptionList = new ArrayList<Option>(uDescription.getGlobalOptions());
		compDescription.setGlobalOptions(globalOptionList);
		
		List<UniversalElement> rootElementsList = new ArrayList<UniversalElement>(uDescription.getRootElements());
		for (UniversalElement uElementI : rootElementsList) {
			
			FileDataSaver fileSaverI = FileDataSaver.importUniversalElement(uElementI);
			compDescription.addRootElement(fileSaverI);
		}
		
		return compDescription;
	}

	public String exportXML(String fileName) throws FileNotFoundException {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		// Class<ComputationDescription> descriptionOntology =
		// org.pikater.core.ontology.description.ComputationDescription.class;

		// xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");

		String xml = xstream.toXML(this);

		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();

		return xml;
	}

	public static ComputationDescription importXML(File file)
			throws FileNotFoundException {

		Scanner scanner = new Scanner(file);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();

		return importXML(xml);
	}
	
	public static ComputationDescription importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		// Class<ComputationDescription> descriptionOntology =
		// org.pikater.core.ontology.description.ComputationDescription.class;

		// xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");


		ComputationDescription computDes = (ComputationDescription) xstream
				.fromXML(xml);

		return computDes;
	}

}
