package org.pikater.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CoreConfiguration {

	public static final String BEANS_CONFIG_FILE = "Beans.xml";

	public static final String CONFIGURATION_MASTER_FILE = "core" + 
			System.getProperty("file.separator") + 
			"configurationMaster.xml";
	
	public static final String INPUTS_KLARA_PATH = "core"
			+ System.getProperty("file.separator") + "inputs"
			+ System.getProperty("file.separator") + "inputsKlara"
			+ System.getProperty("file.separator");
	public static final String DATA_FILES_PATH =
			"core" + System.getProperty("file.separator") +
			"data" + System.getProperty("file.separator") +
			"files" + System.getProperty("file.separator");
	public static final String EXTERNAL_AGENT_JARS_PATH =
			"core" + System.getProperty("file.separator") +
			"ext_agents" + System.getProperty("file.separator");
	public static final String SAVED_PATH = "core"
			+ System.getProperty("file.separator") + "saved"
			+ System.getProperty("file.separator");
	public static final String METADATA_PATH = "core"
			+ System.getProperty("file.separator") + "metadata"
			+ System.getProperty("file.separator");
	
	public static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(BEANS_CONFIG_FILE);
}
