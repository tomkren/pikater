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

public final class Input10 {

	public static ComputationDescription createDescription() {
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");
        
        FileDataProvider fileDataProvider2 = new FileDataProvider();
        fileDataProvider2.setFileURI("weather2.arff");
        
		// Specify a datasource
		DataSourceDescription fileDataSource1 = new DataSourceDescription();
		fileDataSource1.setDataInputType("firstInput");
		fileDataSource1.setDataProvider(fileDataProvider);

		DataSourceDescription fileDataSource2 = new DataSourceDescription();
		fileDataSource2.setDataInputType("secondInput");
		fileDataSource2.setDataProvider(fileDataProvider2);

		// PreProcessing
		DataProcessing processing = new DataProcessing();
		processing.setAgentType(Agent_WeatherSplitter.class.getName());
		processing.addDataSources(fileDataSource1);
		processing.addDataSources(fileDataSource2);

		DataSourceDescription dataSourceSunny = new DataSourceDescription();
		dataSourceSunny.setDataOutputType("sunnyOutput");
		dataSourceSunny.setDataProvider(processing);

		// Save Sunny data
		FileDataSaver saverSunny = new FileDataSaver();
		saverSunny.setDataSource(dataSourceSunny);

		DataSourceDescription dataSourceOvercast = new DataSourceDescription();
		dataSourceOvercast.setDataOutputType("overcastOutput");
		dataSourceOvercast.setDataProvider(processing);

		// Save Overcast data
		FileDataSaver saverOvercast = new FileDataSaver();
		saverOvercast.setDataSource(dataSourceOvercast);

		DataSourceDescription dataSourceRainy = new DataSourceDescription();
		dataSourceRainy.setDataOutputType("rainyOutput");
		dataSourceRainy.setDataProvider(processing);

		// Save Overcast data
		FileDataSaver saverRainy = new FileDataSaver();
		saverRainy.setDataSource(dataSourceRainy);
		
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
				.println("Exporting Ontology input10 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();

		String fileName = CoreConfiguration.INPUTS_KLARA_PATH + "input10"
				+ System.getProperty("file.separator") + "input.xml";

		comDescription.exportXML(fileName);
	}
}
