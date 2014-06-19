package xmlGenerator;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.dataStructures.options.Converter;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


//Example: Single datasource -> single computing agent. -> Single save
public final class Input1 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input1 to Klara's input XML configuration file.");

        //Specify a datasource
        DataSourceDescription fileDataSource=new DataSourceDescription("weather.arff");

        //Create two options for single computing agent
        StepanuvOption optionS = new StepanuvOption();
        optionS.setName("S");
        optionS.setOption( new OptionValue(1) );

        StepanuvOption optionM = new StepanuvOption();
        optionM.setName("M");
        optionM.setOption( new OptionValue(-2) );

        StepanuvOption optionEM = new StepanuvOption();
        optionEM.setName("evaluation_method");
        optionEM.setOption( new OptionValue("CrossValidation") );

        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption( Converter.toOption(optionS) );
		comAgent.addOption( Converter.toOption(optionM) );
        comAgent.addOption( Converter.toOption(optionEM) );
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);


        //Labeled data labeled by our CA are the new datasource
		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataType("Data");
		computingDataSource.setDataProvider(comAgent);

        //Save labeled data
        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        //Our requirements for the description are ready, lets create new computation description
        List<FileDataSaver> roots = new ArrayList<>();
        roots.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

		String fileName = Agent_GUIKlara.filePath + "input1"
				+ System.getProperty("file.separator")
				+ "input1.xml";

		comDescription.exportXML(fileName);
    }
}
