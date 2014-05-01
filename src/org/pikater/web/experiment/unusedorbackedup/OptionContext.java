package org.pikater.web.experiment.unusedorbackedup;

import java.io.File;
import java.io.FilenameFilter;

import org.pikater.core.options.xmlGenerators.CrossValidation_CABox;
import org.pikater.shared.AppHelper;

public class OptionContext
{
	// -----------------------------------------------
	// PRIVATE FIELDS
		
	private static final String thisPath = AppHelper.getPath(OptionContext.class);
	
	/**
	 * The directory where all the serialized XML files are.
	 */
	private static final String deserializationPath = thisPath + "xml";
	
	/**
	 * The directory where all the option java sources are.
	 */
	private static final String serializationPath = thisPath + "java";
	
	static
	{
		if(!getSerializationFolder().isDirectory() || !getDeserializationFolder().isDirectory())
		{
			throw new IllegalStateException("Option context paths are not set correctly. Have you changed the package structure?");
		}
	}
	
	// -----------------------------------------------
	// PUBLIC INTERFACE
	
	public static Package getSerializationPackage()
	{
		return CrossValidation_CABox.class.getPackage();
	}
	
	public static File[] getFilesToDeserialize()
	{
		return getSerializationFolder().listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".java");
			}
		});
	}
	
	public static String getSerializationPathFor(String fileName)
	{
		return deserializationPath + System.getProperty("file.separator") + fileName + ".xml";
	}
	
	// -----------------------------------------------
	// PRIVATE INTERFACE
	
	private static File getSerializationFolder()
	{
		return new File(serializationPath);
	}
	
	private static File getDeserializationFolder()
	{
		return new File(deserializationPath);
	}
}
