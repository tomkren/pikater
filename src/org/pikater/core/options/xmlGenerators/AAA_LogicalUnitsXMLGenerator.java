package org.pikater.core.options.xmlGenerators;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.core.options.LogicalUnitDescription;
import org.reflections.Reflections;

// TODO: rename to something simple like "Exporter"?
public final class AAA_LogicalUnitsXMLGenerator
{
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{

		System.out.println("Exporting LogicalUnits of configuration to XML");

		Package thisPackage = AAA_LogicalUnitsXMLGenerator.class.getPackage();
		System.out.println(thisPackage.getName());

		Reflections reflections = new Reflections(thisPackage.getName());

		Set<Class<? extends LogicalUnitDescription>> logicalUnitclasses = reflections
				.getSubTypesOf(LogicalUnitDescription.class);

		for (Class<? extends LogicalUnitDescription> u : logicalUnitclasses)
		{					
			Class<?> clazz = Class.forName(u.getName());
			Constructor<?> ctor = clazz.getDeclaredConstructor(new Class<?>[0]);
			Object object = ctor.newInstance();

			LogicalUnitDescription unit = (LogicalUnitDescription) object;
			unit.exportXML();
		}
		
		Set<Class<? extends LogicalBoxDescription>> logicalBoxclasses = reflections
				.getSubTypesOf(LogicalBoxDescription.class);

		for (Class<? extends LogicalBoxDescription> u : logicalBoxclasses)
		{
			Class<?> clazz = Class.forName(u.getName());
			Constructor<?> ctor = clazz.getDeclaredConstructor(new Class<?>[0]);
			Object object = ctor.newInstance();

			LogicalBoxDescription unit = (LogicalBoxDescription) object;
			unit.exportXML();
		}

	}
}
