package org.pikater.core.experiments;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.experiment.dataprocessing.Agent_WeatherSplitter;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.batchdescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataSaver;

public final class Input08 implements ITestExperiment {

	public ComputationDescription createDescription() {
		
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
}
