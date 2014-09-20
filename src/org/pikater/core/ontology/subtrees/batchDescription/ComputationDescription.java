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

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.shared.experiment.UniversalComputationDescription;
import org.pikater.shared.experiment.UniversalConnector;
import org.pikater.shared.experiment.UniversalElement;
import org.pikater.shared.experiment.UniversalOntology;
import org.pikater.shared.util.collections.CollectionUtils;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputationDescription implements Concept {

	private static final long serialVersionUID = -7951850172320173523L;

	private List<NewOption> globalOptions;

	private List<FileDataSaver> rootElements;
	
	public ComputationDescription() {
		this.globalOptions = new ArrayList<NewOption>();
		this.rootElements = new ArrayList<FileDataSaver>();
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
		
		List<ISourceDescription> sources = new ArrayList<ISourceDescription>();
		sources.addAll(dataProcessing.exportAllDataSourceDescriptions());
		sources.addAll(dataProcessing.exportAllErrors());
		
		for (ISourceDescription descrI : sources) {
			
			IComputationElement dataProviderI = descrI.exportSource();
			if ( dataProviderI != null) {
				elements.add(dataProviderI);				
			}
		}
		
		return elements;
	}
	
	public void gene() {
		
		int lastUsedNumber = -1;
		for (FileDataSaver fileSaverI : rootElements) {
			lastUsedNumber = fileSaverI.generateIDs(lastUsedNumber);
		}
		
		List<IComputationElement> fifo = new ArrayList<IComputationElement>();
		
		for (FileDataSaver saverI : getRootElements()) {
			fifo.add(saverI);
		}

		while (! fifo.isEmpty()) {

			IComputationElement dataProcessing = fifo.get(0);
			fifo.remove(0);
			
			dataProcessing.cloneSources();
			
			fifo.addAll(notNullElements(dataProcessing) );
			
		}

	}
	
	public UniversalComputationDescription exportUniversalComputationDescription()
	{
		generateIDs();
		gene();
		
		UniversalComputationDescription uModel = new UniversalComputationDescription();
		uModel.addGlobalOptions(new HashSet<NewOption>(CollectionUtils.deepCopy(getGlobalOptions())));
		
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
			
			for (ISourceDescription slotIJ :
				processI.exportAllDataSourceDescriptions()) {
	
				UniversalElement uniElement = new UniversalElement();
				UniversalOntology uniOntology =
						finishedtUniOntologys.get(slotIJ.exportSource().getId());
				uniElement.setOntologyInfo(uniOntology);
				
				UniversalConnector connector = new UniversalConnector();
				connector.setInputDataIdentifier(slotIJ.getInputType());
				connector.setOutputDataIdentifier(slotIJ.getOutputType());
				connector.setFromElement(uniElement);
				
				ontoI.addInputDataSlot(connector);
			}

			for (ISourceDescription slotIJ : processI.exportAllErrors()) {
				
				UniversalElement uniElement = new UniversalElement();
				UniversalOntology uniOntology =
						finishedtUniOntologys.get(slotIJ.exportSource().getId());
				uniElement.setOntologyInfo(uniOntology);
				
				UniversalConnector connector = new UniversalConnector();
				connector.setInputDataIdentifier(slotIJ.getInputType());
				connector.setOutputDataIdentifier(slotIJ.getOutputType());
				connector.setFromElement(uniElement);
				
				ontoI.addInputErrorSlot(connector);
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
				
				for (UniversalConnector connectorI : uElement.getOntologyInfo().getInputDataSlots()) {
					fifo.add( connectorI.getFromElement());
				}
			}
		}

		// connecting to graph
		for ( Integer keyI : finishedDataProcessings.keySet()) {

			DataProcessing processI = (DataProcessing) finishedDataProcessings.get(keyI);
			UniversalOntology uOntoI = finishedtUniOntologys.get(keyI);
			
			List<DataSourceDescription> inputDataSources = new ArrayList<DataSourceDescription>();
			List<ErrorSourceDescription> inputErrorSources = new ArrayList<ErrorSourceDescription>();
			
			for (UniversalConnector slotIJ : uOntoI.getInputDataSlots()) {
				
				UniversalElement uElement = slotIJ.getFromElement();
				IDataProvider dataProvider =  (IDataProvider)
						finishedDataProcessings.get(uElement.getOntologyInfo().getId());
				
				DataSourceDescription dataSourceDesc = new DataSourceDescription();
				dataSourceDesc.setInputType(slotIJ.getInputDataIdentifier());
				dataSourceDesc.setOutputType(slotIJ.getOutputDataIdentifier());
				dataSourceDesc.importSource(dataProvider);
				
				inputDataSources.add(dataSourceDesc);
				
				// TODO:
				/*
				if(slotIJ.isFullySpecified())
				{
					
				}
				*/
			}

			for (UniversalConnector slotIJ : uOntoI.getInputErrorSlots()) {
				
				UniversalElement uElement = slotIJ.getFromElement();
				IDataProvider dataProvider =  (IDataProvider)
						finishedDataProcessings.get(uElement.getOntologyInfo().getId());
				
				ErrorSourceDescription errorSourceDesc = new ErrorSourceDescription();
				errorSourceDesc.setInputType(slotIJ.getInputDataIdentifier());
				errorSourceDesc.setOutputType(slotIJ.getOutputDataIdentifier());
				errorSourceDesc.importSource(dataProvider);
					
				inputErrorSources.add(errorSourceDesc);
			}
			processI.importAllDataSourceDescriptions(inputDataSources);
			processI.importAllErrors(inputErrorSources);
			
			if (processI instanceof FileDataSaver) {
				FileDataSaver saverOnto = (FileDataSaver)processI; 
				description.addRootElement(saverOnto);
			}
		}
		
		return description;
	}

	public String exportXML() {

		generateIDs();
		gene();
		
		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);

		// Class<ComputationDescription> descriptionOntology =
		// org.pikater.core.ontology.description.ComputationDescription.class;

		// xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");

		return xstream.toXML(this);
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


		return (ComputationDescription) xstream.fromXML(xml);
	}

}
