package xmlGenerator;

import java.io.FileNotFoundException;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.agents.experiment.search.Agent_ChooseXValues;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.description.CARecSearchComplex;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileVisualizer;
import org.pikater.core.ontology.description.Search;

import com.thoughtworks.xstream.XStream;

public final class Input2 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input2 to Klara's input XML configuration file.");


        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);
        
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setTrainingData(fileDataSource);
		comAgent.setModelClass(Agent_WekaCA.class.getName());


	    Class<Agent_ChooseXValues> searchClass =
	    		org.pikater.core.agents.experiment.search.Agent_ChooseXValues.class;

		Search search = new Search();
		search.setSearchClass(searchClass.getName());

		CARecSearchComplex complex = new CARecSearchComplex();
		complex.setComputingAgent(comAgent);
		complex.setSearch(search);

		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataProvider(complex);

        FileVisualizer visualizer = new FileVisualizer();
        visualizer.setDataSource(computingDataSource);

        ComputationDescription comDescription = new ComputationDescription();
		comDescription.setRootElement(visualizer);



		String fileName = Agent_GUIKlara.filePath + "input2"
				+ System.getProperty("file.separator")
				+ "input2.xml";

		comDescription.exportXML(fileName);
	}

}
