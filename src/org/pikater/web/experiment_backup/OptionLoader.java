package org.pikater.web.experiment_backup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.web.vaadin.gui.shared.box.LeafBox;

public final class OptionLoader
{
	/**
	 * The current collection of boxes defined.
	 */
	private static List<LogicalUnitDescription> loadedLogicalUnits = new ArrayList<LogicalUnitDescription>();
	
	// Load boxes when first accessed (in the worst case) without explicitly calling the load method.
	static
	{
		load();
	}
	
	// -----------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public static List<LogicalUnitDescription> getAllLogicalUnits()
	{
		return loadedLogicalUnits;
	}
	
	public static List<LeafBox> getAllBoxes()
	{
		List<LeafBox> result = new ArrayList<LeafBox>();
		for (LogicalUnitDescription logUnit : loadedLogicalUnits)
		{
			if(logUnit.getIsBox())
			{
				// result.add(new LeafBox(logUnit));
			}
		}
		return result;
	}

	public static void load() 
	{
		loadLogicalUnits();
	}
	
	// -----------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private static void loadLogicalUnits()
	{
		loadedLogicalUnits.clear();
		
		for(File file : OptionContext.getFilesToDeserialize())
		{
			addIfNotNull(loadLogicalUnit(file));
		}
	}
	
	private static void addIfNotNull(LogicalUnitDescription logUnit)
	{
		if(logUnit != null)
		{
			loadedLogicalUnits.add(logUnit);
		}
	}
	
	private static LogicalUnitDescription loadLogicalUnit(File file)
	{
		try
		{
			return LogicalUnitDescription.importXML(file);
		}
		catch (Throwable e)
		{
			// TODO: something really weird... log this
			return null;
		}
	}
}