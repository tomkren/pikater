package tests.pikater.core.ontology.subtrees.batchDescription;

import java.io.FileNotFoundException;

import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SearchOnly;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SimpleTraining;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;

import xmlGenerator.Input01;
import xmlGenerator.Input04;
import xmlGenerator.Input03;
import xmlGenerator.Input05;

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
				Input04.createDescription(),
				"Input02");

		testComputatingDescription(
				Input03.createDescription(),
				"Input03");

		testComputatingDescription(
				Input04.createDescription(),
				"Input04");
		
		testComputatingDescription(
				Input05.createDescription(),
				"Input05");
		
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
/*		
		try {
			comDescription.exportXML("comDescription.xml");
			comDescription2.exportXML("comDescription2.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//*/
		if (xml1.equals(xml2)) {
			System.out.println("OK - " + note);
		} else {
			System.out.println("Error - " + note);
		}
	}

}
