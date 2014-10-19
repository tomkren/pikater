package org.pikater.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import org.pikater.shared.logging.database.PikaterDBLogger;

/**
 * Provides routines for subclasses to open a ".properties" file
 * and fetch properties from it. All of the routines are properly
 * handled and logged.
 * 
 * @author SkyCrawl
 */
public abstract class PropertiesHandler {
	/**
	 * Routine for opening a .properties files.
	 * 
	 * @param resource the desired .properties file
	 * @param logger logger to use in case of problems
	 * @return the properties file wrapper of null in case of missing file
	 */
	protected static Properties openProperties(File sourceFile) {
		// get input stream of the properties file
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(sourceFile);
		} catch (FileNotFoundException e) {
			PikaterDBLogger.logThrowable(String.format("The following properties file is missing:\n '%s'", sourceFile.getAbsolutePath()), e);
			return null;
		}

		// try to parse it
		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (IOException e) {
			PikaterDBLogger.logThrowable(String.format("The following properties file could not be read (problems with reading rights?):\n '%s'", sourceFile.getAbsolutePath()), e);
			return null;
		} catch (IllegalArgumentException e) {
			PikaterDBLogger.logThrowable(String.format("The following properties file contains a malformed unicode escape sequence:\n '%s'", sourceFile.getAbsolutePath()), e);
			return null;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				PikaterDBLogger.logThrowable(String.format("Weird... Could not close the input stream for the following properties file:\n '%s'", sourceFile.getAbsolutePath()), e);
			}
		}
	}

	/**
	 * Returns the given property from the given loaded properties.
	 * 
	 * @param properties the loaded properties
	 * @param property key of the property to be fetched
	 * @return the designated result or null if the property was not found
	 */
	public static String getProperty(Properties properties, String property) {
		String result = properties.getProperty(property);
		if (result == null) {
			PikaterDBLogger.log(Level.SEVERE, String.format("The property '%s' has not been found.", property));
		} else {
			result = result.trim();
		}
		return result;
	}
}