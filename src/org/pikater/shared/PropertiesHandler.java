package org.pikater.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import org.pikater.shared.logging.PikaterLogger;

public abstract class PropertiesHandler
{
	/**
	 * A routine for opening a .properties files.
	 * @param resource the desired .properties file
	 * @param logger logger to use in case of problems
	 * @return the properties file wrapper of null in case of missing file
	 */
	protected static Properties openProperties(File sourceFile)
	{
		// get input stream of the properties file
		InputStream inputStream;
		try
		{
			inputStream = new FileInputStream(sourceFile);
		}
		catch (FileNotFoundException e)
		{
			PikaterLogger.logThrowable(String.format("The following properties file is missing:\n '%s'", sourceFile.getAbsolutePath()), e);
	    	return null;
		}
	    
		// try to parse it
    	try
		{
    		Properties properties = new Properties();
    		properties.load(inputStream);
			return properties;
		}
		catch (IOException e)
		{
			PikaterLogger.logThrowable(String.format("The following properties file could not be read (problems with reading rights?):\n '%s'", sourceFile.getAbsolutePath()), e);
			return null;
		}
    	catch (IllegalArgumentException e)
		{
    		PikaterLogger.logThrowable(String.format("The following properties file contains a malformed unicode escape sequence:\n '%s'", sourceFile.getAbsolutePath()), e);
    		return null;
		}
    	finally
    	{
    		try
    		{
				inputStream.close();
			}
    		catch (IOException e)
    		{
    			PikaterLogger.logThrowable(String.format("Weird... Could not close the input stream for the following properties file:\n '%s'", sourceFile.getAbsolutePath()), e);
			}
    	}
	}
	
	/**
	 * Fetches a property from the supplied loaded properties or logs an error and returns null.
	 * @param properties The loaded properties.
	 * @param property Key of the property to be fetched.
	 * @return Trimmed (side effect) value of the property to be fetched.
	 */
	public static String getProperty(Properties properties, String property)
	{
		String result = properties.getProperty(property);
		if(result == null)
		{
			PikaterLogger.log(Level.SEVERE, String.format("The property '%s' has not been found.", property));
		}
		else
		{
			result = result.trim();
		}
		return result;
	}
}