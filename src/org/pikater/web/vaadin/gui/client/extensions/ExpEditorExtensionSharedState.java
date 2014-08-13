package org.pikater.web.vaadin.gui.client.extensions;

import com.vaadin.shared.communication.SharedState;

public class ExpEditorExtensionSharedState extends SharedState
{
	private static final long serialVersionUID = -7743696126483421775L;
	
	/**
	 * Updated everytime the count of modified experiments changes. Useful to
	 * handle close events and let the user confirm leaving unsaved content behind.
	 */
	public int modifiedTabsCount;
	
	/**
	 * Needed for client resize events and events that could affect the size of
	 * the currently selected kinetic component's parent.
	 */
	public String currentlySelectedKineticConnectorID = null;
}
