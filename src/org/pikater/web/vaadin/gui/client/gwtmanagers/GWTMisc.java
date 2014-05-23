package org.pikater.web.vaadin.gui.client.gwtmanagers;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

public class GWTMisc
{
	// *************************************************************************************
	// PUBLIC FIELDS
	
	private static BoxInfoCollection boxDefinitions = null;
	
	/**
	 * Just something to avoid bug/implementation confusion.
	 */
	public static final Command alertNotImplemented = new Command()
	{
		@Override
		public void execute()
		{
			Window.alert("Not implemented yet.");
		}
	};
	
	/**
	 * Basic attributes to print when calling node serialization into JSON.
	 */
	public static final JsArrayString jsonAttrsToSerialize = (JsArrayString) JsArrayString.createArray();
	static
	{
		jsonAttrsToSerialize.push("attrs");
		jsonAttrsToSerialize.push("x");
		jsonAttrsToSerialize.push("y");
		jsonAttrsToSerialize.push("id");
		jsonAttrsToSerialize.push("className");
		jsonAttrsToSerialize.push("children");
	}
	
	// *************************************************************************************
	// PUBLIC METHODS
	
	public static String getSimpleName(Class<? extends Object> cls)
	{
		String name = cls.getName(); 
		return name.substring(name.lastIndexOf('.') + 1);
	}
	
	/*
	// just a backup if it ever needs to be used
	public static String getImageResource(String relativePathToResources)
	{
		return GWT.getModuleBaseURL() + relativePathToResources;
	}
	*/
	
	public static BoxInfoCollection getBoxDefinitions()
	{
		if(boxDefinitions == null)
		{
			throw new NullPointerException("Box definitions have not yet been globally set: they are null.");
		}
		else
		{
			return boxDefinitions;
		}
	}
	
	public static void setBoxDefinitions(BoxInfoCollection boxDefinitions)
	{
		GWTMisc.boxDefinitions = boxDefinitions;
	}
}
