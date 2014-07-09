package tests.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SearchOnly;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SimpleTraining;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;

public class TestBatchDescription {

	public static void test() {

		testSimpleTraining();
		testSearchOnly();
	}

	private static void testSimpleTraining() {
		
		ComputationDescription descriptinSimpleTraining = SimpleTraining.createDescription();
		descriptinSimpleTraining.generateIDs();
		
		UniversalComputationDescription udescriptinSimpleTraining =
				descriptinSimpleTraining.exportUniversalComputationDescription();
		
		ComputationDescription descriptinSimpleTraining2 =
				ComputationDescription.importUniversalComputationDescription(
						udescriptinSimpleTraining);

		String xml1 = descriptinSimpleTraining.exportXML();
		String xml2 = descriptinSimpleTraining2.exportXML();
		
		if (xml1.equals(xml2)) {
			System.out.println("OK - SimpleTraining");
		}
	}

	private static void testSearchOnly() {
		
		ComputationDescription descriptionSearchOnly = SearchOnly.createDescription();
		descriptionSearchOnly.generateIDs();
		
		UniversalComputationDescription udescriptionSearchOnly =
				descriptionSearchOnly.exportUniversalComputationDescription();
		
		ComputationDescription descriptionSearchOnly2 =
				ComputationDescription.importUniversalComputationDescription(
						udescriptionSearchOnly);
		
		String xml1 = descriptionSearchOnly.exportXML();
		String xml2 = descriptionSearchOnly2.exportXML();
		
		if (xml1.equals(xml2)) {
			System.out.println("OK - SearchOnly");
		}

	}

}
