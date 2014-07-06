package xmlGenerator;

import org.pikater.core.agents.system.Agent_GUIKlara;
import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.DataPreProcessing;
import org.pikater.core.ontology.subtrees.batchDescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public final class Input10 {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Exporting Ontology input10 to Klara's input XML configuration file.");

        //Specify a datasource
        DataSourceDescription fileDataSource1 = new DataSourceDescription("weather.arff");
        DataSourceDescription fileDataSource2 = new DataSourceDescription("weather2.arff");

        //PreProcessing
        DataPreProcessing processing = new DataPreProcessing();
        processing.addDataSources(fileDataSource1);
        processing.addDataSources(fileDataSource2);

		DataSourceDescription dataSourceEven = new DataSourceDescription();
		dataSourceEven.setDataOutputType("Data-Even");
		dataSourceEven.setDataProvider(processing);

        //Save odd data
        FileDataSaver saverEven = new FileDataSaver();
        saverEven.setDataSource(dataSourceEven);

		DataSourceDescription dataSourceOdd = new DataSourceDescription();
		dataSourceOdd.setDataOutputType("Data-Odd");
		dataSourceOdd.setDataProvider(processing);

        //Save odd data
        FileDataSaver saverOdd = new FileDataSaver();
        saverOdd.setDataSource(dataSourceOdd);
        
        //Our requirements for the description are ready, lets create new computation description
        List<FileDataSaver> roots = new ArrayList<FileDataSaver>();
        roots.add(saverEven);
        roots.add(saverOdd);
        
        ComputationDescription comDescription = new ComputationDescription();
        comDescription.setRootElements(roots);

		String fileName = Agent_GUIKlara.filePath + "input10"
				+ System.getProperty("file.separator")
				+ "input.xml";

		comDescription.exportXML(fileName);
    }
}
