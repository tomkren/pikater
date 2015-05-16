package org.pikater.core.experiments;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.logging.Level;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
import org.pikater.shared.logging.core.ConsoleLogger;
import org.pikater.shared.util.ReflectionUtils;

public class XMLExport {

	public static void export() throws InstantiationException, IllegalAccessException, FileNotFoundException {
		
		Set<Class<? extends ITestExperiment>> inputClasses =
				ReflectionUtils.getSubtypesFromSamePackage(ITestExperiment.class);
		
		for(Class<? extends ITestExperiment> inputClass : inputClasses) {
			ConsoleLogger.log(Level.INFO, "Exporting: " + inputClass.getName());
			
			ComputationDescription inputExperiment =
					inputClass.newInstance().createDescription();
			
			String filePath = CoreConfiguration.getCurrentKlaraInputPath() +
					inputClass.getSimpleName() + ".xml";
			
			inputExperiment.exportXML(filePath);
		}
		ConsoleLogger.log(Level.INFO, "EXPORT SUCCESS!");
	}

	public static void main(String [ ] args) throws InstantiationException, IllegalAccessException, FileNotFoundException {
		
		XMLExport.export();
	}
}
