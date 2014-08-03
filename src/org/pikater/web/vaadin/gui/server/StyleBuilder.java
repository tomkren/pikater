package org.pikater.web.vaadin.gui.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StyleBuilder
{
	private final Map<String, String> propertyToValue;

	public StyleBuilder()
	{
		this.propertyToValue = new HashMap<String, String>();
	}
	
	public void setProperty(String property, String value)
	{
		propertyToValue.put(property, value);
	}
	
	public String build()
	{
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> entry: propertyToValue.entrySet())
		{
			sb.append(String.format("%s: %s; ", entry.getKey(), entry.getValue()));
		}
		return sb.toString();
	}
}