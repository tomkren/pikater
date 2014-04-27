package org.pikater.web.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import org.pikater.web.AppLogger;

import com.vaadin.server.FileResource;

public abstract class PropertiesHandler
{
	/**
	 * A routine for opening application .properties files.
	 * @param filePath File path to the .properties file, including the extension.
	 * @return Loaded properties file.
	 */
	protected Properties openProperties(FileResource resource)
	{
		// get input stream of the properties file
		InputStream inputStream;
		try
		{
			inputStream = new FileInputStream(resource.getSourceFile());
		}
		catch (FileNotFoundException e1)
		{
			logErrorMessage(String.format("The following properties file is missing:\n '%s'", resource.getFilename()));
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
			logErrorMessage(String.format("The following properties file could not be read (problems with reading rights?):\n '%s'", resource.getFilename()));
			return null;
		}
    	catch (IllegalArgumentException e)
		{
    		logErrorMessage(String.format("The following properties file contains a malformed unicode escape sequence:\n '%s'", resource.getFilename()));
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
    			AppLogger.logThrowable(String.format("Weird... Could not close the input stream for the following properties file:\n '%s'", resource.getFilename()), e);
			}
    	}
	}
	
	/**
	 * Fetches a property from the supplied loaded properties or logs an error and returns null.
	 * @param properties The loaded properties.
	 * @param property Key of the property to be fetched.
	 * @return Trimmed (side effect) value of the property to be fetched.
	 */
	public String getProperty(String filePath, Properties properties, String property)
	{
		String result = properties.getProperty(property);
		if(result == null)
		{
			AppLogger.log(Level.SEVERE, String.format("The '%s' property has not been found in the following properties file:\n '%s'", property, filePath));
		}
		else
		{
			result = result.trim();
		}
		return result;
	}
	
	/**
	 * Custom error logging method for this class. If an error is encountered, it means that we have found a potentially serious error in configuration files. The application is not going to work
	 * in such case and signals the user that any requests will fail until the errors are fixed (see the "contextInitialized" method below).
	 * Therefore, we set the variable below to "true" and the "CheckInvalidSettingsFilter" filter will handle the rest.
	 * @param customMessage The error message to be logged.
	 */
	protected void logErrorMessage(String customMessage)
	{
		AppLogger.log(Level.SEVERE, customMessage);
	}
}