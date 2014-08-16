package org.pikater.core;

public class CoreConfiguration {

	public static String BEANS_CONFIG_FILE = "Beans.xml";

	public static String CONFIGURATION_MASTER_FILE = "core" + 
			System.getProperty("file.separator") + 
			"configurationMaster.xml";
	
	public static String INPUTS_KLARA_PATH = "core"
			+ System.getProperty("file.separator") + "inputs"
			+ System.getProperty("file.separator") + "inputsKlara"
			+ System.getProperty("file.separator");
	public static String DATA_FILES_PATH =
			"core" + System.getProperty("file.separator") +
			"data" + System.getProperty("file.separator") +
			"files" + System.getProperty("file.separator");
	public static String EXTERNAL_AGENT_JARS_PATH =
			"core" + System.getProperty("file.separator") +
			"ext_agents" + System.getProperty("file.separator");
	public static String SAVED_PATH = "core"
			+ System.getProperty("file.separator") + "saved"
			+ System.getProperty("file.separator");
	public static String METADATA_PATH = "core"
			+ System.getProperty("file.separator") + "metadata"
			+ System.getProperty("file.separator");
}
