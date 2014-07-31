package org.pikater.core.ontology.subtrees.batchDescription;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
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

	private int priority;
	private List<NewOption> globalOptions;

	private List<FileDataSaver> rootElements;
	
	public ComputationDescription() {
		this.priority = 0;
		this.globalOptions = new ArrayList<NewOption>();
		this.rootElements = new ArrayList<FileDataSaver>();
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		if (priority < 0 || 9 < priority) {
			throw new IllegalArgumentException(
					"Argument priority have to be in the interval <0,9>");
		}
		this.priority = priority;
	}

	public List<NewOption> getGlobalOptions() {
		return globalOptions;
	}
	public void setGlobalOptions(List<NewOption> globalOptions) {
		if (globalOptions == null) {
			throw new IllegalArgumentException(
					"Argument globalOptions can't be null");
		}
		this.globalOptions = globalOptions;
	}

	public List<FileDataSaver> getRootElements() {
		return rootElements;
	}
	public void setRootElements(List<FileDataSaver> rootElements) {
		
		if (rootElements == null) {
			throw new IllegalArgumentException(
					"Argument rootElements can't be null");
		}
		this.rootElements = rootElements;
	}
	public void addRootElement(FileDataSaver rootElement) {
		
		if (rootElement == null) {
			throw new IllegalArgumentException(
					"Argument rootElement can't be null");
		}
		this.rootElements.add(rootElement);
	}

	public void generateIDs() {
		
		int lastUsedNumber = -1;
		for (FileDataSaver fileSaverI : rootElements) {
			lastUsedNumber = fileSaverI.generateIDs(lastUsedNumber);
		}
		
		List<IComputationElement> fifo = new ArrayList<IComputationElement>();
		
		for (FileDataSaver saverI : getRootElements()) {
			fifo.add(saverI);
		}

		int id = 0;
		while (! fifo.isEmpty()) {

			IComputationElement dataProcessing = fifo.get(0);
			fifo.remove(0);
			
			if (dataProcessing.getId() == -1) {
				
				dataProcessing.setId(id);
				id++;
				
				fifo.addAll( notNullElements(dataProcessing) );
			}
		}

	}
	
	private List<IComputationElement> notNullElements(IComputationElement dataProcessing) {
		
		List<IComputationElement> elements = new ArrayList<IComputationElement>();
		
		for (DataSourceDescription descrI : dataProcessing.exportAllDataSourceDescriptions()) {
			
			IDataProvider dataProviderI = descrI.getDataProvider();
			if ( dataProviderI != null) {
				elements.add(descrI.getDataProvider());				
			}
		}
		
		return elements;
	}
	
	
	
	public UniversalComputationDescription exportUniversalComputationDescription() {

		UniversalComputationDescription uModel = new UniversalComputationDescription();
		uModel.setPriority(this.priority);
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
			
			if (dataProcessing == null) {
				continue;
			}
			
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
			System.out.println(processI.getClass());
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

		ComputationDescription description = new ComputationDescription();
		description.setPriority(uDescription.getPriority());
		description.setGlobalOptions(
				new ArrayList<NewOption>(uDescription.getGlobalOptions()) );

		// map - id x ontology
		Map<Integer, UniversalOntology> finishedtUniOntologys =
				new HashMap<Integer, UniversalOntology>();
		Map<Integer, IComputationElement> finishedDataProcessings =
				new HashMap<Integer, IComputationElement>();
		
		List<UniversalElement> fifo = new ArrayList<UniversalElement>();
		
		for (UniversalElement uElementI : uDescription.getRootElements()) {
			fifo.add(uElementI);
		}

		// searching to a depth of graph
		while (! fifo.isEmpty()) {

			UniversalElement uElement = fifo.get(0);
			fifo.remove(0);
			
			UniversalOntology uOntology = uElement.getOntologyInfo();
			if (! finishedDataProcessings.containsKey(uOntology.getId()) ) {
				
				DataProcessing ontology =
						DataProcessing.importUniversalOntology(uOntology);
				
				finishedtUniOntologys.put(
						uOntology.getId(), uOntology);
				finishedDataProcessings.put(
						uOntology.getId(), ontology);
				
				for (UniversalConnector connectorI : uElement.getOntologyInfo().getInputSlots()) {
					fifo.add( connectorI.getFromElement());
				}
			}
		}
		
		// connecting to graph
		for ( Integer keyI : finishedDataProcessings.keySet()) {

			DataProcessing processI = (DataProcessing) finishedDataProcessings.get(keyI);
			UniversalOntology uOntoI = finishedtUniOntologys.get(keyI);
			
			List<DataSourceDescription> inputSlots = new ArrayList<DataSourceDescription>();
			
			Collection<UniversalConnector> slotsI = uOntoI.getInputSlots();
			for (UniversalConnector slotIJ : slotsI) {
	
				UniversalElement uElement = slotIJ.getFromElement();
				IDataProvider dataProvider =  (IDataProvider)
						finishedDataProcessings.get(uElement.getOntologyInfo().getId());
				
				DataSourceDescription dataSourceDesc = new DataSourceDescription();
				dataSourceDesc.setDataInputType(slotIJ.getInputDataType());
				dataSourceDesc.setDataOutputType(slotIJ.getOutputDataType());
				dataSourceDesc.setDataProvider(dataProvider);
				
				inputSlots.add(dataSourceDesc);
			}
			
			processI.importAllDataSourceDescriptions(inputSlots);
			
			if (processI instanceof FileDataSaver) {
				FileDataSaver saverOnto = (FileDataSaver)processI; 
				description.addRootElement(saverOnto);
			}
		}
		
		return description;
	}

	public String exportXML() {

		generateIDs();
		
		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		// Class<ComputationDescription> descriptionOntology =
		// org.pikater.core.ontology.description.ComputationDescription.class;

		// xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");

		String xml = xstream.toXML(this);

		return xml;
	}

	public void exportXML(String fileName) throws FileNotFoundException {

		String xml = exportXML();

		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();
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
		xstream.setMode(XStream.NO_REFERENCES);
		
		// Class<ComputationDescription> descriptionOntology =
		// org.pikater.core.ontology.description.ComputationDescription.class;

		// xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");


		ComputationDescription computDes = (ComputationDescription) xstream
				.fromXML(xml);

		return computDes;
	}

}
