package org.pikater.shared.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.AppConfig;
import org.pikater.web.experiment.box.LeafBox;

public final class BoxLoader
{
	/**
	 * The path where all the boxes reside constructed dynamically and safely against package structures.
	 */
	private static final String contextPath = AppConfig.baseAppPath + LogicalUnitDescription.class.getPackage() + System.getProperty("file.separator");
	
	/**
	 * The current collection of boxes defined.
	 */
	private static List<LeafBox> boxes = new ArrayList<LeafBox>();
	
	// Load boxes when first accessed (in the worst case) without explicitly calling the load method.
	static
	{
		load();
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
	
	public static void load() 
	{
		boxes.clear();
		
		List<LogicalUnitDescription> logicalUnits = loadLogicalUnits();
		for (LogicalUnitDescription logUnit : logicalUnits)
		{
			boxes.add(new LeafBox(logUnit));
		}
	}
	
	// -----------------------------------------------------------------
	// PRIVATE INTERFACE
	
	public static List<LogicalUnitDescription> loadLogicalUnits()
	{
		List<LogicalUnitDescription> result = new ArrayList<LogicalUnitDescription>();

		addIfNotNull(result, loadLogicalUnit(contextPath + "CrossValidationBox.xml"));
		addIfNotNull(result, loadLogicalUnit(contextPath + "DifferenceVisualizerBox.xml"));
		addIfNotNull(result, loadLogicalUnit(contextPath + "FileInputBox.xml"));
		addIfNotNull(result, loadLogicalUnit(contextPath + "FileVisualizerBox.xml"));
		addIfNotNull(result, loadLogicalUnit(contextPath + "RandomSearchBox.xml"));
		addIfNotNull(result, loadLogicalUnit(contextPath + "SimulatedAnnealingBox.xml"));

		return result;
	}
	
	private static void addIfNotNull(List<LogicalUnitDescription> collection, LogicalUnitDescription logUnit)
	{
		if(logUnit != null)
		{
			collection.add(logUnit);
		}
	}
	
	private static LogicalUnitDescription loadLogicalUnit(String filePath)
	{
		try
		{
			return LogicalUnitDescription.importXML(new File(filePath));
		}
		catch (FileNotFoundException e)
		{
			// TODO: log this
			return null;
		}
	}
}