package pikater.tests;

import pikater.tests.ontology.description.ImportExportJSON;
import pikater.tests.ontology.description.ImportExportXML;

public class TestSystem {


	public static void main(String [] args)
	{
		System.out.println("TestSystem:");
		
		ImportExportXML mportExportXMLTest =
				new ImportExportXML();
		mportExportXMLTest.run();
		
		ImportExportJSON importExportJSON =
				new ImportExportJSON();
		importExportJSON.run();
		
	}
	
}
