package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

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
	public KineticComponentClickMode clickMode;
	public boolean openOptionsOnSelection;
	
	/*
	 * Other programmatic fields shared. 
	 */
	public boolean serverThinksThatSchemaIsModified;
	
	public KineticComponentState()
	{
		this.clickMode = getDefaultClickMode();
		this.openOptionsOnSelection = getDefaultOptionsOpenedOnSelection();
		this.serverThinksThatSchemaIsModified = false;
	}
	
	public static KineticComponentClickMode getDefaultClickMode()
	{
		return KineticComponentClickMode.SELECTION;
	}
	
	public static boolean getDefaultOptionsOpenedOnSelection()
	{
		return true;
	}
}