package org.pikater.core.ontology.subtrees.batchDescription;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputationDescription implements Concept {

	private static final long serialVersionUID = -7951850172320173523L;

	private List<NewOption> globalOptions = new ArrayList<NewOption>();
	private List<FileDataSaver> rootElements = new ArrayList<FileDataSaver>();
	
	public List<NewOption> getGlobalOptions() {
		return globalOptions;
	}

	public void setGlobalOptions(List<NewOption> globalOptions) {
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

	public void generateIDs() {
		
		int lastUsedNumber = -1;
		for (FileDataSaver fileSaverI : rootElements) {
			lastUsedNumber = fileSaverI.generateIDs(lastUsedNumber);
		}
	}
	
	public UniversalComputationDescription exportUniversalComputationDescription() {

		UniversalComputationDescription uModel = new UniversalComputationDescription();
		uModel.addGlobalOptions(new HashSet<NewOption>(this.getGlobalOptions()));

		
		// map - id x ontology
		Map<Integer, UniversalOntology> finishedtUniOntologys =
				new HashMap<Integer, UniversalOntology>();
		Map<Integer, IComputationElement> finishedDataProcessings =
				new HashMap<Integer, IComputationElement>();
		
		List<IComputationElement> fifo = new ArrayList<IComputationElement>();
		
		for (FileDataSaver saverI : getRootElements()) {
			fifo.add(saverI);
		}

		// searching to a depth of graph
		while (! fifo.isEmpty()) {

			IComputationElement dataProcessing = fifo.get(0);
			fifo.remove(0);
			
			if (! finishedDataProcessings.containsKey(dataProcessing.getId()) ) {
				
				UniversalOntology uOntology =
						dataProcessing.exportUniversalOntology();
				
				finishedtUniOntologys.put(
						uOntology.getId(), uOntology);
				finishedDataProcessings.put(
						dataProcessing.getId(), dataProcessing);
				
				for (DataSourceDescription descrI : dataProcessing.exportAllDataSourceDescriptions()) {
					fifo.add( descrI.getDataProvider());
				}
			}
		}
		
		// connecting to graph
		for ( Integer keyI : finishedtUniOntologys.keySet()) {
			
			UniversalOntology ontoI = finishedtUniOntologys.get(keyI);
			IComputationElement processI = finishedDataProcessings.get(keyI);
			
			List<DataSourceDescription> slotsI = processI.exportAllDataSourceDescriptions();
			for (DataSourceDescription slotIJ : slotsI) {
	
				UniversalElement uniElement = new UniversalElement();
				UniversalOntology uniOntology =
						finishedtUniOntologys.get(slotIJ.getDataProvider().getId());
				uniElement.setOntologyInfo(uniOntology);
				
				UniversalConnector connector = new UniversalConnector();
				connector.setInputDataType(slotIJ.getDataInputType());
				connector.setOutputDataType(slotIJ.getDataOutputType());
				connector.setFromElement(uniElement);
				
				ontoI.addInputSlot(connector);
			}
			
			UniversalElement elementI =  new UniversalElement();
			elementI.setOntologyInfo(ontoI);
			uModel.addElement(elementI);
		}
		
		return uModel;
	}

	public static ComputationDescription importUniversalComputationDescription(
			UniversalComputationDescription uDescription) {
		
		ComputationDescription compDescription = new ComputationDescription();
		
		List<NewOption> globalOptionList = new ArrayList<NewOption>(uDescription.getGlobalOptions());
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
