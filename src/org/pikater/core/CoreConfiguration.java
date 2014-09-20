package org.pikater.core;

import org.pikater.core.agents.system.computation.graph.GUIDGenerator;
import org.pikater.shared.database.connection.PostgreSQLConnectionProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CoreConfiguration
{
	/**
	 * Private constructors hide the public ones.
	 */
	private CoreConfiguration()
	{
	}
	
	/*
	 * Spring configuration convenience interface
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
	
	public static GUIDGenerator getGUIDGenerator()
	{
		return getBean("guidGenerator");
	}
	
	/*
	 * Other configuration interface
	 */
	
	public static String getCoreMasterConfigurationFilepath()
	{
		return "core" + System.getProperty("file.separator") + "configurationMaster.xml";
	}
	
	public static String getKlarasInputsPath()
	{
		return getCorePath("inputs") + "inputsKlara" + System.getProperty("file.separator");
	}
	
	public static String getDataFilesPath()
	{
		return getCorePath("data") + "files" + System.getProperty("file.separator");
	}
	
	public static String getExtAgentsPath()
	{
		return getCorePath("ext_agents");
	}
	
	public static String getSavedResultsPath()
	{
		return getCorePath("saved");
	}
	
	public static String getMetadataPath()
	{
		return getCorePath("metadata");
	}
	
	private static String getCorePath(String nextFolder)
	{
		return "core" + System.getProperty("file.separator") + nextFolder + System.getProperty("file.separator");
	}
}