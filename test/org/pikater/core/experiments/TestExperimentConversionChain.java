package org.pikater.core.experiments;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.logging.Level;

import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
import org.pikater.shared.experiment.UniversalComputationDescription;
import org.pikater.shared.logging.core.ConsoleLogger;
import org.pikater.shared.util.ReflectionUtils;

/**
 * 
 * Tests of BatchDescription an Universal format
 *
 */
public class TestExperimentConversionChain {

	public static void test() throws InstantiationException, IllegalAccessException {
		
		Set<Class<? extends ITestExperiment>> inputClasses = ReflectionUtils.getSubtypesFromSamePackage(ITestExperiment.class);
		for(Class<? extends ITestExperiment> inputClass : inputClasses)
		{
			ConsoleLogger.log(Level.INFO, "Testing: " + inputClass.getName());
			if(!testComputatingDescription(inputClass.newInstance().createDescription()))
			{
				ConsoleLogger.log(Level.INFO, "FAILURE.");
				return;
			}
		}
		ConsoleLogger.log(Level.INFO, "SUCCESS!");
	}

	/**
	 * Tests the conversion chain between core and universal experiment formats.
	 * 
	 * @param experiment the test experiment giving
	 * 
	 * @return whether conversion to universal format and back was successful
	 */
	private static boolean testComputatingDescription(ComputationDescription inputExperiment)
	{
		UniversalComputationDescription universalExperiment = inputExperiment.exportUniversalComputationDescription();
		ComputationDescription outputExperiment =
				ComputationDescription.importUniversalComputationDescription(
						universalExperiment);

		String xml1 = inputExperiment.exportXML();
		String xml2 = outputExperiment.exportXML();
		if(xml1.equals(xml2))
		{
			ConsoleLogger.log(Level.INFO, "- Result: MATCHED");
			return true;
		}
		else
		{
			ConsoleLogger.log(Level.SEVERE, "- Result: MISMATCHED. Writing mismatched XML representations to files...");
			try
			{
				inputExperiment.exportXML("experiment_input.xml");
				outputExperiment.exportXML("experiment_output.xml");
				ConsoleLogger.log(Level.INFO, "- Done.");
			}
			catch (FileNotFoundException e)
			{
				ConsoleLogger.logThrowable("Unexpected error occured:", e);
			}
			return false;
		}
	}
}
