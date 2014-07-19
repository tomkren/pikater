package xmlGenerator;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchDescription.NewModel;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
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
        NewOption optionF = new NewOption(
        		new IntegerValue(8), "F"); 
        
        evaluationMethod.addOption(optionF);
        
        
        //Create two options for single computing agent
        NewOption optionS = new NewOption(
        		new IntegerValue(1), "S"); 

        NewOption optionM = new NewOption(
        		new IntegerValue(-2), "M");
        
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
        List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
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
