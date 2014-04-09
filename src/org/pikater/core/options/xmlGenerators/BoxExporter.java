package org.pikater.core.options.xmlGenerators;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.pikater.core.options.LogicalUnit;
import org.reflections.Reflections;

public final class BoxExporter
{
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		// TODO: rewrite this using XStream

		System.out.println("Exporting LogicalUnit configuration to XML");

		FileInputBox ib = new FileInputBox();
		ib.exportXML();

		Package thisPackage = BoxExporter.class.getPackage();

		Reflections reflections = new Reflections(thisPackage.getName());

		Set<Class<? extends LogicalUnit>> logicalUnitclasses = reflections
				.getSubTypesOf(LogicalUnit.class);

		for (Class<? extends LogicalUnit> u : logicalUnitclasses)
		{

			Class<?> clazz = Class.forName(u.getName());
			Constructor<?> ctor = clazz.getDeclaredConstructor(new Class<?>[0]);
			Object object = ctor.newInstance();

			LogicalUnit unit = (LogicalUnit) object;
			unit.exportXML();
		}
	}
}
