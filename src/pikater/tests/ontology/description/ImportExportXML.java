package pikater.tests.ontology.description;

import pikater.ontology.description.boxesWrappers.ComputationDescription;
import pikater.ontology.description.examples.SearchOnly;
import pikater.ontology.description.examples.SimpleTraining;
import pikater.ontology.description.examples.VisualizationOnly;

public class ImportExportXML {

	public void run() {

        ComputationDescription searchOnlyDescription = 
        		SearchOnly.createDescription();
        makeTransformations(searchOnlyDescription);

        ComputationDescription simpleTrainingDescription =
        		SimpleTraining.createDescription();
        makeTransformations(simpleTrainingDescription);
        
        ComputationDescription visualizationOnlyDescription =
        		VisualizationOnly.createDescription();
        makeTransformations(visualizationOnlyDescription);
        
	}
	
	private void makeTransformations(ComputationDescription description)
	{
        String xml = description.exportXML();
		//System.out.println(xml);

        ComputationDescription cd2 =
        		(ComputationDescription) ComputationDescription.importXML(xml);
		
		String xml2 = cd2.exportXML();
		//System.out.println(xml2);

		if (! xml.equals(xml2) ) {
			System.out.println("BUG - ImportExportXML");
		}
	}

}
