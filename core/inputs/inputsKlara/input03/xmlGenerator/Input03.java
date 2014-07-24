package xmlGenerator;

import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.io.FileNotFoundException;


public final class Input03 {

	public static ComputationDescription createDescription() {
		
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("?????.arff");

        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

		ComputingAgent comAgent1 = new ComputingAgent();
		comAgent1.setTrainingData(fileDataSource);
		comAgent1.setTestingData(fileDataSource);
		comAgent1.setModel(new NewModel());
		

		NewOption optionOutput1 = new NewOption("output","evaluation_only");

		CARecSearchComplex complex1 = new CARecSearchComplex();
		complex1.setComputingAgent(comAgent1);

		complex1.addOption(optionOutput1);

		DataSourceDescription computingDataSource1 = new DataSourceDescription();
		computingDataSource1.setDataOutputType("Data");
		computingDataSource1.setDataProvider(complex1);


		ComputingAgent comAgent2 = new ComputingAgent();
		comAgent2.setTrainingData(computingDataSource1);
		comAgent2.setTestingData(computingDataSource1);
		comAgent2.setModel(new NewModel());

		NewOption optionOutput2 = new NewOption("output","evaluation_only");

		CARecSearchComplex complex2 = new CARecSearchComplex();
		complex2.setComputingAgent(comAgent2);
		complex2.addOption(optionOutput2);

		DataSourceDescription computingDataSource2 = new DataSourceDescription();
		computingDataSource2.setDataOutputType("Data");
		computingDataSource2.setDataProvider(complex2);



        FileDataSaver saver = new FileDataSaver();
        saver.setDataSource(computingDataSource2);

        ComputationDescription comDescription = new ComputationDescription();
        comDescription.addRootElement(saver);
		
        return comDescription;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input03 to Klara's input XML configuration file.");
		
		ComputationDescription comDescription = createDescription();

		String fileName = Agent_GUIKlara.filePath + "input03"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);


	}

}
