package xmlGenerator;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.experiment.dataprocessing.Agent_WeatherSplitter;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.DataProcessing;
import org.pikater.core.ontology.subtrees.batchDescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class Input08 {

	public static ComputationDescription createDescription() {
		
	    FileDataProvider fileDataProvider = new FileDataProvider();
	    fileDataProvider.setFileURI("weather.arff");
	    
	    FileDataProvider fileDataProvider2 = new FileDataProvider();
	    fileDataProvider2.setFileURI("weather.arff");
    
		// Specify datasources for the first preprocessing 
		DataSourceDescription fileDataSource1 = new DataSourceDescription();
		fileDataSource1.setInputType("firstInput");
		fileDataSource1.setDataProvider(fileDataProvider);

		DataSourceDescription fileDataSource2 = new DataSourceDescription();
		fileDataSource2.setInputType("secondInput");
		fileDataSource2.setDataProvider(fileDataProvider2);

		// PreProcessing 1 ...
		DataProcessing processing = new DataProcessing();
		processing.setAgentType(Agent_WeatherSplitter.class.getName());
		processing.addDataSources(fileDataSource1);
		processing.addDataSources(fileDataSource2);

		
    // ... and its 3 outputs 
		DataSourceDescription dataSourceSunny = new DataSourceDescription();
		dataSourceSunny.setOutputType("sunnyOutput");
		dataSourceSunny.setInputType("firstInput");
		dataSourceSunny.setDataProvider(processing);

		DataSourceDescription dataSourceRainy = new DataSourceDescription();
		dataSourceRainy.setOutputType("rainyOutput");
		dataSourceRainy.setInputType("secondInput");
		dataSourceRainy.setDataProvider(processing);
		
		/*
		DataSourceDescription dataSourceOvercast = new DataSourceDescription();
		dataSourceOvercast.setOutputType("overcastOutput");
		dataSourceOvercast.setDataProvider(processing);
		*/
    // Preprocessing2: combines sunny and rainy, then splits them again  
    
		DataProcessing processing2 = new DataProcessing();
		processing2.setAgentType(Agent_WeatherSplitter.class.getName());
		processing2.addDataSources(dataSourceSunny);
		processing2.addDataSources(dataSourceRainy);


    // Save all the outputs:
		DataSourceDescription dataSourceSunny1 = new DataSourceDescription();
		dataSourceSunny1.setOutputType("sunnyOutput");
		dataSourceSunny1.setDataProvider(processing2);

		// Save Sunny data
		FileDataSaver saverSunny = new FileDataSaver();
		saverSunny.setDataSource(dataSourceSunny1);

		DataSourceDescription dataSourceOvercast1 = new DataSourceDescription();
		dataSourceOvercast1.setOutputType("overcastOutput");
		dataSourceOvercast1.setDataProvider(processing2);

		// Save Overcast data
		FileDataSaver saverOvercast = new FileDataSaver();
		saverOvercast.setDataSource(dataSourceOvercast1);

		DataSourceDescription dataSourceRainy1 = new DataSourceDescription();
		dataSourceRainy1.setOutputType("rainyOutput");
		dataSourceRainy1.setDataProvider(processing2);

		// Save Rainy data
		FileDataSaver saverRainy = new FileDataSaver();
		saverRainy.setDataSource(dataSourceRainy1);

	
		// Our requirements for the description are ready, lets create new
		// computation description
		List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
		roots.add(saverSunny);
		roots.add(saverOvercast);
		roots.add(saverRainy);

		ComputationDescription comDescription = new ComputationDescription();
		comDescription.setRootElements(roots);

		return comDescription;
	}
	
	public static void main(String[] args) throws FileNotFoundException {

		System.out
				.println("Exporting Ontology input08 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();

		String fileName = CoreConfiguration.INPUTS_KLARA_PATH + "input08"
				+ System.getProperty("file.separator") + "input.xml";

		comDescription.exportXML(fileName);
	}
}
