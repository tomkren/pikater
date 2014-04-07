package org.pikater.shared.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.AppConfig;
import org.pikater.web.experiment.box.LeafBox;

public final class BoxLoader
{
	/**
	 * The path where all the boxes reside constructed dynamically and safely against package structures.
	 */
	private static final String contextPath = AppConfig.baseAppPath + LogicalUnit.class.getPackage() + System.getProperty("file.separator");
	
	/**
	 * The current collection of boxes defined.
	 */
	private static List<LeafBox> boxes = new ArrayList<LeafBox>();
	
	// Load boxes when first accessed (in the worst case) without explicitly calling the load method.
	static
	{
		try
		{
			load();
		}
		catch (FileNotFoundException e)
		{
			// TODO: log this
			e.printStackTrace();
		}
	}
	
	// -----------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public static List<LeafBox> getAllBoxes()
	{
		return boxes;
	}

	public static LeafBox getBoxBy(String name)
	{
		for (LeafBox box : boxes)
		{
			if (box.displayName.equals(name))
			{
				return box;
			}
		}
		return null;
	}
	
	public static void load() throws FileNotFoundException 
	{
		boxes.clear();
		
		List<LogicalUnit> logicalUnits = loadLogicalUnits(false);
		for (LogicalUnit logUnit : logicalUnits)
		{
			boxes.add(new LeafBox(logUnit));
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		loadLogicalUnits(true);
	}
	
	// -----------------------------------------------------------------
	// PRIVATE INTERFACE
	
	public static List<LogicalUnit> loadLogicalUnits(boolean invokedFromConsole) throws FileNotFoundException
	{
		List<LogicalUnit> result = new ArrayList<LogicalUnit>();

		addIfNotNull(result, loadLogicalUnit(contextPath + "CrossValidationBox.xml", invokedFromConsole));
		addIfNotNull(result, loadLogicalUnit(contextPath + "DifferenceVisualizerBox.xml", invokedFromConsole));
		addIfNotNull(result, loadLogicalUnit(contextPath + "FileInputBox.xml", invokedFromConsole));
		addIfNotNull(result, loadLogicalUnit(contextPath + "FileVisualizerBox.xml", invokedFromConsole));
		addIfNotNull(result, loadLogicalUnit(contextPath + "RandomSearchBox.xml", invokedFromConsole));
		addIfNotNull(result, loadLogicalUnit(contextPath + "SimulatedAnnealingBox.xml", invokedFromConsole));

		return result;
	}
	
	private static void addIfNotNull(List<LogicalUnit> collection, LogicalUnit logUnit)
	{
		if(logUnit != null)
		{
			collection.add(logUnit);
		}
	}
	
	private static LogicalUnit loadLogicalUnit(String filePath, boolean invokedFromConsole) throws FileNotFoundException
	{
		try
		{
			return LogicalUnit.importXML(new File(filePath));
		}
		catch (FileNotFoundException e)
		{
			if(invokedFromConsole)
			{
				throw e;
			}
			else
			{
				// TODO: log this
				return null;
			}
		}
	}
}