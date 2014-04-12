package org.pikater.shared;

import java.io.PrintWriter;

import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.AppHelper;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.slots.AbstractSlot;
import org.pikater.shared.util.Interval;

import com.thoughtworks.xstream.XStream;

public final class OptionXMLSerializer
{
	private static final XStream serializer;
	static
	{
		// prepare serializer
		serializer = new XStream();
		for (Class<? extends LogicalUnitDescription> unit : AppHelper.getSubtypesFromSamePackage(LogicalUnitDescription.class))
		{
			serializer.processAnnotations(unit);
		}
		for (Class<? extends AbstractSlot> unit : AppHelper.getSubtypesFromSamePackage(AbstractSlot.class))
		{
			serializer.processAnnotations(unit);
		}
		for (Class<? extends AbstractParameter> unit : AppHelper.getSubtypesFromSamePackage(AbstractParameter.class))
		{
			serializer.processAnnotations(unit);
		}
		for (Class<? extends Object> unit : AppHelper.getTypesFromPackage(Interval.class.getPackage()))
		{
			serializer.processAnnotations(unit);
		}
	}
	
	public static void serialize()
	{
		// process types and serialize them
		for (Class<? extends LogicalUnitDescription> unit : AppHelper.getSubtypesFromSamePackage(LogicalUnitDescription.class))
		{
			try
			{
				// prepare type to serialize
				LogicalUnitDescription unitInstance = (LogicalUnitDescription) unit.getDeclaredConstructor(new Class<?>[0]).newInstance();
				
				// and serialize
				PrintWriter file = new PrintWriter(OptionContext.getSerializationPathFor(unitInstance.getClass().getSimpleName()));
				file.println(serializer.toXML(unitInstance));
				file.close();
			}
			catch (Throwable t)
			{
				// TODO: log this
				t.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		serialize();
	}
}