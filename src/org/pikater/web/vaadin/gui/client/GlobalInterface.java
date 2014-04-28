package org.pikater.web.vaadin.gui.client;

public class GlobalInterface
{
	public static String getSimpleName(Class<? extends Object> cls)
	{
		String name = cls.getName(); 
		return name.substring(name.lastIndexOf('.') + 1);
	}
}
