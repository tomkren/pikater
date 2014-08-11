package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.web.vaadin.gui.shared.kineticcomponent.ClickMode;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.visualstyle.KineticBoxSettings;

import com.vaadin.shared.AbstractComponentState;

public class KineticComponentState extends AbstractComponentState
{
	private static final long serialVersionUID = 7400546695911691608L;
	
	/*
	 * GWT SERIALIZATION ISSUES:
	 * - GENERAL - Classes used in GWT RPC have to implement isSerializable or Serializable.
	 * - GENERAL - Default constructor of any visibility makes GWT happy.
	 * - GENERAL - Final fields are not serialized in GWT.
	 * - SPECIFIC - String.format method is not translatable to GWT.
	 * For more information:
	 * http://www.gwtproject.org/doc/latest/DevGuideServerCommunication.html#DevGuideSerializableTypes
	 */
	
	/*
	 * Toolbar settings.
	 */
	public ClickMode clickMode;
	public boolean boxManagerBoundWithSelection;
	public boolean box_iconsVisible;
	public double box_scale;
	
	/*
	 * Other programmatic fields shared. 
	 */
	public boolean serverThinksThatSchemaIsModified;
	
	public KineticComponentState()
	{
		this.clickMode = getDefaultClickMode();
		this.boxManagerBoundWithSelection = getBoxManagerBoundWithSelectionByDefault();
		this.box_iconsVisible = getBoxIconsVisibleByDefault();
		this.box_scale = getDefaultScale();
		
		this.serverThinksThatSchemaIsModified = false;
	}
	
	public KineticBoxSettings toSettingsClass()
	{
		return new KineticBoxSettings(box_iconsVisible, box_scale);
	}
	
	//----------------------------------------------------
	// DEFAULT VALUE DECLARATION
	
	public static ClickMode getDefaultClickMode()
	{
		return ClickMode.SELECTION;
	}
	
	public static boolean getBoxManagerBoundWithSelectionByDefault()
	{
		return true;
	}
	
	public static boolean getBoxIconsVisibleByDefault()
	{
		return true;
	}
	
	public static double getDefaultScale()
	{
		return 1;
	}
}