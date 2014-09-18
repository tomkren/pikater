package org.pikater.core;

import org.pikater.shared.database.connection.PostgreSQLConnectionProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class CoreConfiguration
{
	/*
	 * Spring interface
	 */
	private static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(getConfigurationFileName());
	
	@SuppressWarnings("unchecked")
	private static <T extends Object> T getBean(String id)
	{
		return (T) APPLICATION_CONTEXT.getBean(id);
	}
	
	public static String getConfigurationFileName()
	{
		return "Beans.xml";
	}
	
	public static PostgreSQLConnectionProvider getPGSQLConnProvider()
	{
		return getBean("defaultConnection");
	}
	
	/*
	 * Other configuration interface
	 */
	
	public static String getPath_CoreMasterConfigurationFile()
	{
		// TODO: if not dynamic, put this into "Beans.xml"?
		return "core" + System.getProperty("file.separator") + 	"configurationMaster.xml";
	}
	
	public static String getPath_KlarasInputs()
	{
		// TODO: if not dynamic, put this into "Beans.xml"?
		return "core" + System.getProperty("file.separator") + "inputs" + System.getProperty("file.separator") + 
				"inputsKlara" + System.getProperty("file.separator");
	}
	
	public static String getPath_DataFiles()
	{
		// TODO: if not dynamic, put this into "Beans.xml"?
		return "core" + System.getProperty("file.separator") + "data" + System.getProperty("file.separator") +
				"files" + System.getProperty("file.separator");
	}
	
	public static String getPath_ExtAgentsJARs()
	{
		// TODO: if not dynamic, put this into "Beans.xml"?
		return "core" + System.getProperty("file.separator") + "ext_agents" + System.getProperty("file.separator");
	}
	
	public static String getPath_Saved()
	{
		// TODO: if not dynamic, put this into "Beans.xml"?
		return "core" + System.getProperty("file.separator") + "saved" + System.getProperty("file.separator");
	}
	
	public static String getPath_Metadata()
	{
		// TODO: if not dynamic, put this into "Beans.xml"?
		return "core" + System.getProperty("file.separator") + "metadata" + System.getProperty("file.separator");
	}
}