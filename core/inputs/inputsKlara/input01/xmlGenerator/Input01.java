package xmlGenerator;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.task.EvaluationMethod;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


//Example: Single datasource -> single computing agent. -> Single save
public final class Input01 {

	public static ComputationDescription createDescription() {
		
        //Specify a datasource
        DataSourceDescription fileDataSource=new DataSourceDescription("weather.arff");

        //Create validation method for a computing agent
        EvaluationMethod evaluationMethod = new EvaluationMethod();
        evaluationMethod.setType("CrossValidation");
        
        //Create cross validation option                
        NewOption optionF = new NewOption("F",8);
        
        evaluationMethod.addOption(optionF);
        
        
        //Create two options for single computing agent
        NewOption optionS = new NewOption("S",1);

        NewOption optionM = new NewOption("M",2);

        //Create new computing agent, add options and datasource that we have created above
		ComputingAgent comAgent = new ComputingAgent();
		comAgent.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent.addOption(optionS);
		comAgent.addOption(optionM);
		comAgent.setTrainingData(fileDataSource);
		comAgent.setTestingData(fileDataSource);
		comAgent.setEvaluationMethod(new EvaluationMethod("CrossValidation"));
		//comAgent.setEvaluationMethod(evaluationMethod);
		comAgent.setModel(new NewModel());

        //Labeled data labeled by our CA are the new datasource
		DataSourceDescription computingDataSource = new DataSourceDescription();
		computingDataSource.setDataOutputType("Data");
		computingDataSource.setDataProvider(comAgent);

        //Save labeled data
        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource);

        //Our requirements for the description are ready, lets create new computation description
        List<FileDataSaver> roots = new ArrayList<>();
        roots.add(saver);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

        return comDescription;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input01 to Klara's input XML configuration file.");

		ComputationDescription comDescription = createDescription();
		
		String fileName = Agent_GUIKlara.filePath + "input01"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
    }
}
