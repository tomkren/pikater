package xmlGenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.DataSourceDescription;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.FileVisualizer;
import org.pikater.core.ontology.messages.Option;


public final class Input1 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input1 to Klara's input XML configuration file.");

		StepanuvOption optionB = new StepanuvOption();
		optionB.setName("B");
		optionB.setOption( new OptionValue(new Integer(2)) );

		StepanuvOption optionR = new StepanuvOption();
		optionR.setName("R");
		optionR.setOption( new OptionValue(new Integer(2)) );

		StepanuvOption optionS = new StepanuvOption();
		optionS.setName("S");
		optionS.setOption( new OptionValue(new Integer(0)) );
		
		StepanuvOption optionM = new StepanuvOption();
		optionM.setName("M");
		optionM.setOption( new OptionValue(new Integer(0)) );	
		
		ArrayList<Option> options = new ArrayList<Option>();
		options.add(optionB.toOption());
		options.add(optionR.toOption());
		options.add(optionS.toOption());
		options.add(optionM.toOption());
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setOptions(options);
		comAgent.setTrainingData(fileDataSource);
		comAgent.setModelClass(Agent_WekaCA.class.getName());


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
