package org.pikater.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.logging.Level;

import org.pikater.core.experiments.ITestExperiment;
import org.pikater.shared.logging.core.ConsoleLogger;
import org.pikater.shared.util.IOUtils;
import org.pikater.shared.util.ReflectionUtils;

public class TestExperimentSerializer {
	
	public static void main(String[] args) throws FileNotFoundException, InstantiationException, IllegalAccessException {
		
		if(args.length > 0) {
			File outputDir = new File(args[0]);
			if(outputDir.isDirectory()) {
				Set<Class<? extends ITestExperiment>> inputClasses = ReflectionUtils.getSubtypesFromSamePackage(ITestExperiment.class);
				for(Class<? extends ITestExperiment> inputClass : inputClasses) {
					ConsoleLogger.log(Level.INFO, "Attempting to serialize: " + inputClass.getName());
					String outputFileName = IOUtils.joinPathComponents(outputDir.getAbsolutePath(), inputClass.getSimpleName());
					inputClass.newInstance().createDescription().exportXML(outputFileName);
				}
				ConsoleLogger.log(Level.INFO, "SUCCESS!");
			} else {
				throw new IllegalArgumentException("Not a directory: " + args[0]);
			}
		} else {
			throw new IllegalStateException("An argument denoting path for output XML files is expected.");
		}
	}
}
