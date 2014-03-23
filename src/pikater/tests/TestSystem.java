package pikater.tests;

import pikater.ontology.description.FileDataProvider;
import pikater.tests.ontology.description.ImportExportJSON;
import pikater.tests.ontology.description.ImportExportXML;

public class TestSystem {


	public static void main(String [] args)
	{
		System.out.println("TestSystem:");

//		ImportExportXML mportExportXMLTest =
//				new ImportExportXML();
//		mportExportXMLTest.run();
		
		ImportExportJSON importExportJSON =
				new ImportExportJSON();
		importExportJSON.run();


		Class<?>[] cs = FileDataProvider.class.getInterfaces();
		
		for (Class<?> c: cs) {
			System.out.println(c.getName());
		}
//*/
	}
	
}
