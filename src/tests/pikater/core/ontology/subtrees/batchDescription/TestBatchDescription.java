package tests.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SearchOnly;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SimpleTraining;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;

import xmlGenerator.Input01;
import xmlGenerator.Input02;
import xmlGenerator.Input03;

public class TestBatchDescription {

	public static void test() {

		testComputatingDescription(
				SimpleTraining.createDescription(),
				"SimpleTraining");
		testComputatingDescription(
				SearchOnly.createDescription(),
				"SearchOnly");

		testComputatingDescription(
				Input01.createDescription(),
				"Input01");
		testComputatingDescription(
				Input02.createDescription(),
				"Input02");
		testComputatingDescription(
				Input03.createDescription(),
				"Input03");

	}

	private static void testComputatingDescription(
			ComputationDescription comDescription, String note) {
		
		comDescription.generateIDs();
		
		UniversalComputationDescription udescriptinSimpleTraining =
				comDescription.exportUniversalComputationDescription();
		
		ComputationDescription comDescription2 =
				ComputationDescription.importUniversalComputationDescription(
						udescriptinSimpleTraining);

		String xml1 = comDescription.exportXML();
		String xml2 = comDescription2.exportXML();
		
		if (xml1.equals(xml2)) {
			System.out.println("OK - " + note);
		} else {
			System.out.println("Error - " + note);
		}
	}

}
