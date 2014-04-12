package input1.xmlGenerator;

import java.io.FileNotFoundException;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileVisualizer;
import org.pikater.core.ontology.description.Method;


public final class Input1 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input1 to Klara's input XML configuration file.");


        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        Class<Agent_WekaCA> comAgentClass =
        		org.pikater.core.agents.experiment.computing.Agent_WekaCA.class;

		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setTrainingData(fileDataSource);
		comAgent.setModelClass(Agent_WekaCA.class.getName());
		comAgent.setMethod(new Method("RBFNetwork"));

		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataProvider(comAgent);

        FileVisualizer visualizer = new FileVisualizer();
        visualizer.setDataSource(computingDataSource);

        ComputationDescription comDescription = new ComputationDescription();
		comDescription.setRootElement(visualizer);

		


		String fileName = Agent_GUIKlara.filePath + "input1"
				+ System.getProperty("file.separator")
				+ "input1.xml";

		comDescription.exportXML(fileName);


	}

}
